#Anomalous Network Traffic Monitoring
import pandas as pd
import numpy as np
import ipaddress
import dns.resolver
import dns.reversename
import pygeoip
import matplotlib.pyplot as plt 
from matplotlib import gridspec

def bytes_to_mb(bytes):
    return bytes / 1000000

def ts_to_hours(ts):
    ts = ts/100
    hours, rem = divmod(ts, 3600)
    minutes, secs = divmod(rem, 60)
    return "{:02}:{:02}:{:02}".format(int(hours), int(minutes), int(secs))

testfile='test9.parquet'

### Read parquet test files
test=pd.read_parquet(testfile)

### IP geolocalization
gi=pygeoip.GeoIP('./GeoIP.dat')
gi2=pygeoip.GeoIP('./GeoIPASNum.dat')

# Check what protocols were used
protocols = test['proto'].unique()
print("\nProtocols used: " + str(protocols))
test['proto'].value_counts().plot(kind='bar')
plt.title('Protocols used and their frequency')
plt.xlabel('Protocol')
plt.ylabel('Occurences')
plt.show()

# Check what protocols were used
port = test['port'].unique()
print("\nPort used: " + str(port))
test['port'].value_counts().plot(kind='bar')
plt.title('Port used and their frequency')
plt.xlabel('Port')
plt.ylabel('Occurences')
plt.show()

#Number of upload and download packets/bytes
print("\nUploads: " + str(test['up_bytes'].describe()))
print("\nDownloads: " + str(test['down_bytes'].describe()))

#source IP addresses
source_ips = test['src_ip'].unique()
#print("source IPs used: " + str(source_ips))
print("\nTotal source IP: " + str(len(source_ips)))

#source IP addresses
dst_ips = test['dst_ip'].unique()
#print("source IPs used: " + str(source_ips))
print("\nTotal destination IP: " + str(len(dst_ips)))

# Verify that port 53 only has UDP connections
tcp53 = test.loc[(test['port'] == 53) & (test['proto'] != 'udp')]
if tcp53.empty:
    print("\nNo TCP connections on port 53\n")
    udpF=test.loc[test['proto']=='udp']
    print("udpFlow:\n" + str(udpF))
    #Number of UDP flows for each source IP
    nudpF=test.loc[test['proto']=='udp'].groupby(['src_ip'])['up_bytes'].count()
    print("\nnudpF: \n" + str(nudpF))
else:
    print("TCP connections on port 53: " + str(tcp53))

# Verify that port 443 only has TCP connections
udp443 = test.loc[(test['port'] == 443) & (test['proto'] != 'tcp')]
if udp443.empty:
    print("\nNo UDP connections on port 443\n")
else:
    print("\nUDP connections on port 443:\n " + str(udp443))
    
 #Number of tcp flows to port 443, for each source IP
nutcpF443=test.loc[(test['proto']=='tcp')&(test['port']==443)].groupby(['src_ip'])['up_bytes'].count()
print("\nnutcpF443: \n" + str(nutcpF443))

# Add the country code and the organization to flow test
test['dst_cc'] = test['dst_ip'].apply(lambda x: gi.country_code_by_addr(x))
test['dst_org'] = test['dst_ip'].apply(lambda x: gi2.org_by_addr(x))

# Check the connections to each country
countries = test['dst_cc'].unique()
print("\nCountries: \n" + str(countries))

# Check the connections to each organization
organizations = test['dst_org'].unique()
print("\nOrganizations: \n" + str(organizations))

#Is destination IPv4 a public address?
NET=ipaddress.IPv4Network('192.168.109.0/24')
bpublic=test.apply(lambda x: ipaddress.IPv4Address(x['dst_ip']) not in NET,axis=1)

## Anomalous test analysis
print("######################################################3")
print("\nAnomalous test analysis\n")
print("\nInternal Communications Analysis:\n")

# Flows from internal IPs to internal IPs (192.168.109.X)
internal_flows_test = test.loc[(test['src_ip'].str.startswith('192.168.109.')) & (test['dst_ip'].str.startswith('192.168.109.'))]

# Number of UDP flows for each source IP (internal)
nudpF_internal = internal_flows_test.loc[internal_flows_test['proto']=='udp'].groupby(['src_ip']).count()
print("\nNumber of UDP flows for each source IP: \n" + str(nudpF_internal))

# Number of TCP flows for each source IP (internal)
ntcpF_internal = internal_flows_test.loc[internal_flows_test['proto']=='tcp'].groupby(['src_ip']).count()
print("\nNumber of TCP flows for each source IP counting upload bytes: \n" + str(ntcpF_internal))

# Verify which IPs belong to servers (IPs that are private (start with 192.168.109.X) that have more communication, both as source or destination)
src_ips = test.loc[test['src_ip'].str.startswith('192.168.109.')].groupby('src_ip').size().reset_index(name='counts')
dst_ips = test.loc[test['dst_ip'].str.startswith('192.168.109.')].groupby('dst_ip').size().reset_index(name='counts')

# Check what's the average value of the counts
print("\nAverage value of the counts: \n" + str(src_ips['counts'].mean()))

# Combine the counts
server_ips_test = src_ips.set_index('src_ip').add(dst_ips.set_index('dst_ip'), fill_value=0).reset_index().sort_values(by='counts', ascending=False).reset_index(drop=True)

print("\nServer IPs: \n" + str(server_ips_test))

# Average value of the counts without the top 4 IPs
print("\nAverage value of the counts without the top 4 IPs: \n" + str(server_ips_test['counts'][4:].mean()))



# Plot the top 8 server IPs
server_ips_test.head(8).plot(kind='bar', x='index', y='counts')
plt.title('Potential server IPs')
plt.xlabel('IP')
plt.ylabel('Flows')
plt.savefig('./Graficos/top8_potential_server_ips_testAnoma.png')
plt.show()

##
# Number of UDP flow to port 53 (DNS), for each source IP (internal flows)
nudpF53_internal = internal_flows_test.loc[(internal_flows_test['proto']=='udp')&(internal_flows_test['port']==53)].groupby(['src_ip']).count()
print("\nNumber of UDP flow to port 53 (DNS), for each source IP, using UDP: " + str(nudpF53_internal))

# Total uploaded bytes to destination port 53 using UDP, for each source IP (internal flows)
upS_internal = internal_flows_test.loc[((internal_flows_test['proto']=='udp')&(internal_flows_test['port']==53))].groupby(['src_ip'])['up_bytes'].sum()
print("\nTotal uploaded bytes to destination port 53 using UDP, for each source IP: " + str(upS_internal))

##
# Number of TCP flows to port 443, for each source IP (internal flows)
ntcpF443_internal = internal_flows_test.loc[(internal_flows_test['proto']=='tcp')&(internal_flows_test['port']==443)].groupby(['src_ip']).count()
print("\nNumber of TCP flows to port 443 (HTTPS), for each source IP, using TCP: " + str(ntcpF443_internal))

# Total uploaded bytes to destination port 443 using TCP, for each source IP (internal flows)
upS_internal = internal_flows_test.loc[((internal_flows_test['port']==443))].groupby(['src_ip'])['up_bytes'].sum()
print("\nTotal uploaded bytes to destination port 443, for each source IP: " + str(upS_internal))


##
# Uploaded bytes per flow, for each source IP (internal flows)
upF_internal = internal_flows_test.groupby(['src_ip'])['up_bytes'].sum()
print("\nUploaded bytes per flow, for each source IP: \n" + str(upF_internal))

# Downloaded bytes per flow, for each source IP (internal flows)
downS_internal = internal_flows_test.groupby(['src_ip'])['down_bytes'].sum()
print("\nDownloaded bytes per flow, for each source IP: \n" + str(downS_internal))


##
# Normal timestamps per source IP flows
ts_internal = internal_flows_test.groupby(['src_ip'])['timestamp'].mean()
print("\nNormal timestamps per source IP flows: \n" + str(ts_internal))

# Check flows per country (internal flows)
cc_internal = internal_flows_test.groupby(['dst_cc']).count()
print("\nFlows per country: \n" + str(cc_internal))

# Check flows per organization (internal flows)
org_internal = internal_flows_test.groupby(['dst_org']).count()
print("\nFlows per organization: \n" + str(org_internal))


### External Communications Analysis
print("\n#####################################\n")
print("\External Communications Analysis:\n")

# Flows from external IPs to external IPs (192.168.109.X)
external_flows_test = test.loc[(test['src_ip'].str.startswith('192.168.109.')) & (~test['dst_ip'].str.startswith('192.168.109.'))]

# Number of UDP flows for each source IP (external flows)
nudpF_external = external_flows_test.loc[external_flows_test['proto']=='udp'].groupby(['src_ip']).count()
print("\nNumber of UDP flows for each source IP: \n" + str(nudpF_external))

# Number of TCP flows for each source IP (external flows)
ntcpF_external = external_flows_test.loc[external_flows_test['proto']=='tcp'].groupby(['src_ip']).count()
print("\nNumber of TCP flows for each source IP counting upload bytes: \n" + str(ntcpF_external))


##
# Number of UDP flow to port 53 (DNS), for each source IP (external flows)
nudpF53_external = external_flows_test.loc[(external_flows_test['proto']=='udp')&(external_flows_test['port']==53)].groupby(['src_ip']).count()
print("\nNumber of UDP flow to port 53 (DNS), for each source IP, using UDP: \n" + str(nudpF53_external))

# Total uploaded bytes to destination port 53 using UDP, for each source IP (external flows)
upS_external = external_flows_test.loc[((external_flows_test['proto']=='udp')&(external_flows_test['port']==53))].groupby(['src_ip'])['up_bytes'].sum()
print("\nTotal uploaded bytes to destination port 53 using UDP, for each source IP: \n" + str(upS_external))


##
# Number of TCP flows to port 443, for each source IP (external flows)
ntcpF443_external = external_flows_test.loc[(external_flows_test['proto']=='tcp')&(external_flows_test['port']==443)].groupby(['src_ip']).count()
print("\nNumber of TCP flows to port 443 (HTTPS), for each source IP, using TCP: \n" + str(ntcpF443_external))

# Total uploaded bytes to destination port 443 using TCP, for each source IP (external flows)
upS_external = external_flows_test.loc[((external_flows_test['port']==443))].groupby(['src_ip'])['up_bytes'].sum()
print("\nTotal uploaded bytes to destination port 443, for each source IP: \n" + str(upS_external))


##
# Uploaded bytes per flow, for each source IP (external flows)
upF_external = external_flows_test.groupby(['src_ip'])['up_bytes'].sum()
print("\nUploaded bytes per flow, for each source IP: \n" + str(upF_external))

# Downloaded bytes per flow, for each source IP (external flows)
downS_external = external_flows_test.groupby(['src_ip'])['down_bytes'].sum()
print("\nDownloaded bytes per flow, for each source IP: \n" + str(downS_external))


##
# Normal timestamps per source IP flows
ts_external = external_flows_test.groupby(['src_ip'])['timestamp'].mean()
print("\nNormal timestamps per source IP flows: \n" + str(ts_external))

# Check flows per country (external flows)
cc_external = external_flows_test.groupby(['dst_cc']).count()
print("\nFlows per country: \n" + str(cc_external))

# Check flows per organization (external flows)
org_external = external_flows_test.groupby(['dst_org']).count()
print("\nFlows per organization: \n" + str(org_external))
print("\n\n")

datafile='data9.parquet'

### Read parquet data files
data=pd.read_parquet(datafile)
# Flows from internal IPs to internal IPs (192.168.109.X)
internal_flows = data.loc[(data['src_ip'].str.startswith('192.168.109.')) & (data['dst_ip'].str.startswith('192.168.109.'))]

# Flows from external IPs to external IPs (192.168.109.X)
external_flows = data.loc[(data['src_ip'].str.startswith('192.168.109.')) & (~data['dst_ip'].str.startswith('192.168.109.'))]
# Add the country and org
data['dst_cc']  = data['dst_ip'].apply(lambda x: gi.country_code_by_addr(x))
data['dst_org'] = data['dst_ip'].apply(lambda x: gi2.org_by_addr(x))
# Combine the counts
server_ips = src_ips.set_index('src_ip').add(dst_ips.set_index('dst_ip'), fill_value=0).reset_index().sort_values(by='counts', ascending=False).reset_index(drop=True)

#Comparing Non Anomolous and Anomolous Network Traffic
print("\nComparing Non Anomolous and Anomolous Network Traffic\n")

# Check the used protocols
if (data['proto'].unique() == test['proto'].unique()).all():
    print("Protocols used are the same\n")
else:
    print("The different protocols used are: \n" + str(np.setdiff1d(data['proto'].unique(), test['proto'].unique())))

# Check the used ports
if (data['port'].unique() == test['port'].unique()).all():
    print("Ports used are the same\n")
else:
    print("The different ports used are: \n" + str(np.setdiff1d(data['port'].unique(), test['port'].unique())))

## Anomolous verifications
print("\nAnomolous verifications\n")
print("\nServer attacks\n")

# How the server_ips_test are obtained
# # Verify which IPs belong to servers (IPs that are private (start with 192.168.109.X) that have more communication, both as source or destination)
# src_ips = data[data['src_ip'].str.startswith('192.168.109.')].groupby('src_ip').size().reset_index(name='counts')
# dst_ips = data[data['dst_ip'].str.startswith('192.168.109.')].groupby('dst_ip').size().reset_index(name='counts')

#server_ips_test = src_ips.set_index('src_ip').add(dst_ips.set_index('dst_ip'), fill_value=0).reset_index().sort_values(by='counts', ascending=False).reset_index(drop=True)


# How the server_ips_test_test are obtained
## Verify which IPs belong to servers (IPs that are private (start with 192.168.109.X) that have more communication, both as source or destination)
src_ips_test = test[test['src_ip'].str.startswith('192.168.109.')].groupby('src_ip').size().reset_index(name='counts')
dst_ips_test = test[test['dst_ip'].str.startswith('192.168.109.')].groupby('dst_ip').size().reset_index(name='counts')

server_ips_test_test = src_ips_test.set_index('src_ip').add(dst_ips_test.set_index('dst_ip'), fill_value=0).reset_index().sort_values(by='counts', ascending=False).reset_index(drop=True)



# Server IPs differences (top 10 of the most used IPs on test)
top_10_server_ips_test_test = server_ips_test_test.head(10)['index'].tolist()
# Filter the normal data for the rows that have the top 10 server IPs of the test
filter_data = server_ips_test[server_ips_test['index'].isin(top_10_server_ips_test_test)]
# Merge to check differences
merge = pd.merge(filter_data, server_ips_test_test, how='inner', left_on='index', right_on='index', suffixes=('_normal', '_test'))

# Make a plot for this differences (top 10 so that there are some references of normal behaviour)
merge.plot(x='index', y=['counts_normal', 'counts_test'], kind='bar', title="IPs (normally, Server IPs) with more communication", xlabel="IP", ylabel="Flows")
plt.savefig('./Graficos/ips_with_more_communication.png')
plt.show()

# Check which IPs normally communicate with the Server IPs and how many flows
server_ips_test_flows = data.loc[data['dst_ip'].isin(server_ips_test['index'])].groupby(['src_ip']).size().reset_index(name='counts')
#server_ips_test_flows = server_ips_test_flows.sort_values(by='src_ip', ascending=False)
print("IPs that communicate with the Server IPs and how many flows: \n" + str(server_ips_test_flows))

# Check which IPs communicate with the Server IPs and how many flows (test) and order them by IP
server_ips_test_flows_test = test.loc[test['dst_ip'].isin(server_ips_test_test['index'])].groupby(['src_ip']).size().reset_index(name='counts_test')
#server_ips_test_flows_test = server_ips_test_flows_test.sort_values(by='src_ip', ascending=False)
print("IPs that communicate with the \"Server IPs\" (and others with too traffic) and how many flows (test): \n" + str(server_ips_test_flows_test))

# Merge to check differences
possible_server_attackers = pd.merge(server_ips_test_flows, server_ips_test_flows_test, how='outer', on='src_ip')

# Fill NaN with 0, since NaN represents no flows aka 0
possible_server_attackers = possible_server_attackers.fillna(0)

# Check the increase of communication in percentage to the servers from normal to test
possible_server_attackers['increase'] = ((possible_server_attackers['counts_test'] - possible_server_attackers['counts']) / possible_server_attackers['counts']) * 100
possible_server_attackers.sort_values(by='increase', ascending=False, inplace=True)
print("Increase of communication in percentage to the servers from normal to test: \n" + str(possible_server_attackers))

## This values need to be justified (and maybe changed)
# Collecting the flows with an increase over 150% or inf, have more than 50 flows to the servers
server_attackers_ips = possible_server_attackers.loc[((possible_server_attackers['increase'] > 250) | (possible_server_attackers['increase'] == np.inf)) & (possible_server_attackers['counts'] > 50)]
print("Possible server attackers IPs that have an increase over 250% or inf, have more than 50 flows to the servers: \n" + str(server_attackers_ips))

# Graph with the increase of communication in percentage to the servers from normal to test
server_attackers_ips.plot(x='src_ip', y='increase', kind='bar', title="Increase of communication in percentage to the servers from normal to test", xlabel="IPs with 150% increase of comms to the servers and at least 50 flows to the servers", ylabel="Increase (%)")
plt.savefig('./Graficos/increase_communication_to_servers.png')
plt.show()

# Keeping only the IPs with increase over 500% 
server_attackers_ips = server_attackers_ips.loc[server_attackers_ips['increase'] > 500]

# Check the comms over time of the possible IPs that are attacking the servers
timeline_server_attackers = test.loc[(test['src_ip'].isin(server_attackers_ips['src_ip']))]
timeline_server_attackers['timestamp'] = timeline_server_attackers['timestamp'].apply(lambda x: ts_to_hours(x))

### Botnet detection
print("\nBotnet detection\n")

# Having internal_flows_test and internal_flows_test from the previous cells, we can check new IPs that are communicating internally
# Check the new IPs that are communicating internally
botnet_suspects_ips = np.setdiff1d(internal_flows_test['src_ip'].unique(), internal_flows['src_ip'].unique())
print("New IPs that are communicating internally: \n" + str(botnet_suspects_ips))

# Check if the botnet_suspect_ips are communicating with the other botnet_suspect_ips
botnet_suspects_ips_flows = internal_flows_test[internal_flows_test['src_ip'].isin(botnet_suspects_ips)]
botnet_suspects_ips_flows = botnet_suspects_ips_flows.groupby(['src_ip', 'dst_ip']).size().reset_index(name='counts')
print("Botnet Suspect IPs that are communicating internally, with which IPs and how many flows: \n" + str(botnet_suspects_ips_flows))

# Check the Protocol and Port of the between the botnet_suspect_ips and the IPs they communicate with
botnet_suspects_ips_flows_proto_port = internal_flows_test[internal_flows_test['src_ip'].isin(botnet_suspects_ips)]
botnet_suspects_ips_flows_proto_port = botnet_suspects_ips_flows_proto_port.groupby(['src_ip', 'dst_ip', 'proto', 'port']).size().reset_index(name='counts')
print("Botnet Suspect IPs that are communicating internally, with which IPs, how many flows, and the protocol and port: \n" + str(botnet_suspects_ips_flows_proto_port))

#New IPs that are communicating internally: ['192.168.109.20' '192.168.109.72']

# Get the timeline from test from the 192.168.109.20 to the other botnet_suspects_ips
bot_20 = test.loc[(test['src_ip'] == '192.168.109.20') & (test['dst_ip'].isin(botnet_suspects_ips_flows['dst_ip']))]
# Add timestamp column
bot_20['timestamp'] = bot_20['timestamp'].apply(lambda x: ts_to_hours(x))

# Get the timeline from test from the 192.168.109.72 to the other botnet_suspects_ips
bot_72 = test.loc[(test['src_ip'] == '192.168.109.72') & (test['dst_ip'].isin(botnet_suspects_ips_flows['dst_ip']))]
# Add timestamp column
bot_72['timestamp'] = bot_72['timestamp'].apply(lambda x: ts_to_hours(x))

# Get the first 15 lines of the timeline from test
bot_20[['src_ip', 'dst_ip', 'timestamp']].head(15).reset_index(drop=True)
bot_72[['src_ip', 'dst_ip', 'timestamp']].head(15).reset_index(drop=True)

# Check what protocols and ports are used
print("Botnet 20 Suspect IPs that are communicating externally, with which IPs, how many flows, and the protocol and port: \n" + str(bot_20.groupby(['src_ip', 'dst_ip', 'proto', 'port']).size().reset_index(name='counts')))
print("Botnet 72 Suspect IPs that are communicating externally, with which IPs, how many flows, and the protocol and port: \n" + str(bot_72.groupby(['src_ip', 'dst_ip', 'proto', 'port']).size().reset_index(name='counts')))


# Keep the timestamp between 04:00:00 and 04:30:00
bot_20 = bot_20.loc[(bot_20['timestamp'] >= '12:30:00') & (bot_20['timestamp'] <= '12:32:00')]
bot_72 = bot_72.loc[(bot_72['timestamp'] >= '10:20:00') & (bot_72['timestamp'] <= '10:32:00')]


# Ignore this :D
bot_20['count'] = 1
bot_72['count'] = 1

#Make a scatter plot
bot_20.plot(x='timestamp', y='count', kind='scatter', title="Botnet Activity by 192.168.109.20 while communicating.", xlabel="Timestamp", ylabel="IPs")
plt.xticks(rotation=60)
# put the dst_ip over the points
for i, txt in enumerate(bot_20['dst_ip']):
    plt.annotate(txt, (bot_20['timestamp'].iloc[i], bot_20['count'].iloc[i]), rotation=60, fontsize=10)
plt.savefig('./Graficos/bot_20_botnet_suspects_ips_communicating_internally.png')
plt.show()

bot_72.plot(x='timestamp', y='count', kind='scatter', title="Botnet Activity by 192.168.109.72 while communicating.", xlabel="Timestamp", ylabel="IPs")
plt.xticks(rotation=60)
# put the dst_ip over the points
for i, txt in enumerate(bot_72['dst_ip']):
    plt.annotate(txt, (bot_72['timestamp'].iloc[i], bot_72['count'].iloc[i]), rotation=60, fontsize=10)
plt.savefig('./Graficos/bot_72_botnet_suspects_ips_communicating_internally.png')
plt.show()

### Anomolous External Activity - Data Exfiltration
print("###################################################")
print("\nAnomolous External Activity - Data Exfiltration\n")

# Having already external_flows_test, we can check new IPs that are communicating externally and how much down_bytes and up_bytes they have
# Check the new IPs that are communicating externally
internet_suspect_ips = np.setdiff1d(external_flows_test['src_ip'].unique(), external_flows['src_ip'].unique())
print("New IPs that are communicating externally: \n" + str(internet_suspect_ips))

exterior_suspect_ips = np.setdiff1d(external_flows_test['dst_ip'].unique(), external_flows['dst_ip'].unique())
print("New IPs that are communicating from the exterior: \n" + str(exterior_suspect_ips))

# Check the down_bytes and up_bytes of the internet_suspects_ips using from_bytes_to_mb()
internet_suspects_ips_bytes = external_flows_test[external_flows_test['src_ip'].isin(internet_suspect_ips)]
internet_suspects_ips_down_bytes = internet_suspects_ips_bytes.groupby(['src_ip'])['down_bytes'].sum().apply(lambda x: bytes_to_mb(x))
internet_suspects_ips_up_bytes = internet_suspects_ips_bytes.groupby(['src_ip'])['up_bytes'].sum().apply(lambda x: bytes_to_mb(x))
print("Internet Suspect IPs that are communicating externally and DOWNLOADING how much MBytes: \n" + str(internet_suspects_ips_down_bytes) + "\n" + 
      "Internet Suspect IPs that are communicating externally and UPLOADING how much MBytes:\n" + str(internet_suspects_ips_up_bytes))

# Check which public IPs are they accessing
suspect_public_ips = internet_suspects_ips_bytes.groupby(['src_ip', 'dst_ip', 'proto', 'port']).size().reset_index(name='counts').sort_values(by='counts', ascending=False)
print("Internet Suspect IPs that are communicating externally, with the public IPs: \n" + str(suspect_public_ips))

# Check which Country and Organization they are accessing
suspect_public_ips_cc_org = internet_suspects_ips_bytes.groupby(['src_ip', 'dst_ip', 'dst_cc', 'dst_org']).size().reset_index(name='counts').sort_values(by='counts', ascending=False)
print("Internet Suspect IPs that are communicating externally, with the public IPs, and the Country and Organization: \n" + str(suspect_public_ips_cc_org))

# Check the number of flows for each source IP (external flows)
info_outside_normal = external_flows_test.groupby(['src_ip', 'dst_ip']).size().reset_index(name='counts').sort_values(by='counts', ascending=False)

# Check the number of flows for each source IP (external flows of test)
info_outside_test = external_flows_test.groupby(['src_ip', 'dst_ip']).size().reset_index(name='counts').sort_values(by='counts', ascending=False)

# Merge to check differences
info_outside = pd.merge(info_outside_normal, info_outside_test, on=['src_ip', 'dst_ip'], how='right')

# Percentage of increase of communication to the exterior
info_outside['increase'] = ((info_outside['counts_y'] - info_outside['counts_x']) / info_outside['counts_x']) * 100

# Keep the ones with increase > 150% or inf, and have more than 50 flows
info_outside = info_outside[((info_outside['increase'] > 150) | (info_outside['increase'] == np.inf)) & (info_outside['counts_y'] > 50)] 

# Maybe should conclude something else or smtg at all here
# Doesn't reach great values, therefore not alarming, I guess

## Data Exfiltration

# Check the number of bytes uploaded 
normal_up = data.loc[~(data['dst_ip']).str.startswith('192.168.109.')].groupby(['src_ip'])['up_bytes'].sum().reset_index(name='up_bytes')
# Check the number of flows to the internet
normal_flows = data.loc[~(data['dst_ip']).str.startswith('192.168.109.')].groupby(['src_ip']).size().reset_index(name='counts')

# Check the number of bytes uploaded - test
test_up = test.loc[~(test['dst_ip']).str.startswith('192.168.109.')].groupby(['src_ip'])['up_bytes'].sum().reset_index(name='up_bytes')
# Check the number of flows to the internet - test
test_flows = test.loc[~(test['dst_ip']).str.startswith('192.168.109.')].groupby(['src_ip']).size().reset_index(name='counts')

# Average uploaded bytes per flow
normal_up = pd.merge(normal_up, normal_flows, on='src_ip', how='inner')
normal_up['avg_up_bytes'] = normal_up['up_bytes'] / normal_up['counts']
# Average uploaded bytes per flow - test
test_up = pd.merge(test_up, test_flows, on='src_ip', how='inner')
test_up['avg_up_bytes'] = test_up['up_bytes'] / test_up['counts']

# Merge to check differences -- Only increases of 50% or more
up_bytes = pd.merge(normal_up, test_up, on='src_ip', how='inner')
up_bytes = up_bytes[(up_bytes['avg_up_bytes_y'] > (up_bytes['avg_up_bytes_x']*1.5))] 

# Extract the destination IP where the up_bytes IP is communicating with
exfilt = data.loc[(data['src_ip'].isin(up_bytes['src_ip'])) & (~data['dst_ip'].str.startswith('192.168.109.'))]
exfilt = exfilt[['src_ip', 'dst_ip']]
# Add country and org
exfilt['dst_cc'] = exfilt['dst_ip'].apply(lambda x: gi.country_code_by_addr(x))
exfilt['dst_org'] = exfilt['dst_ip'].apply(lambda x: gi2.org_by_addr(x))

# From the exfilt, check the uploaded bytes to the internet (dst_ip not starting with 192.168.109.) from the src_ips there
exfilt_up = test.loc[(test['src_ip'].isin(exfilt['src_ip'])) & (~test['dst_ip'].str.startswith('192.168.109.'))].groupby(['src_ip'])['up_bytes'].sum().reset_index(name='up_bytes_exfiltered')
# The same for test dataset
exfilt_up['up_bytes_normal'] = data.loc[(data['src_ip'].isin(exfilt['src_ip'])) & (~data['dst_ip'].str.startswith('192.168.109.'))].groupby(['src_ip'])['up_bytes'].sum().reset_index(name='up_bytes_normal')['up_bytes_normal']

# Add the increase in percentage
exfilt_up['increase'] = ((exfilt_up['up_bytes_exfiltered'] - exfilt_up['up_bytes_normal']) / exfilt_up['up_bytes_normal']) * 100

# Plot the exfilt_up
exfilt_up.plot(x='src_ip', y=['up_bytes_exfiltered', 'up_bytes_normal'], kind='bar', title="Exfiltration", xlabel="IP", ylabel="Bytes")
plt.savefig('./Graficos/exfiltration.png')
plt.show()


# Remove the ones who transmitted less than 500MB
exfilt_up_500MB = exfilt_up[exfilt_up['up_bytes_exfiltered'] > int(500e6) ]

# Check who this source IP has communications with
exfilt_500MB = test.loc[(test['src_ip'].isin(exfilt_up_500MB['src_ip'])) & (~test['dst_ip'].str.startswith('192.168.109.'))]

# Join the pairs src_ip - dst_ip with number of flows
exfilt_500MB = exfilt_500MB.groupby(['src_ip', 'dst_ip']).size().reset_index(name='counts')

# Get the uploaded bytes from the pairs (src_ip - dst_ip)
_upload_bytes = test.loc[(test['src_ip'].isin(exfilt_500MB['src_ip'])) & (test['dst_ip'].isin(exfilt_500MB['dst_ip']))].groupby(['src_ip', 'dst_ip'])['up_bytes'].sum().reset_index(name='up_bytes')

# Merge the info about exfiltration
exfilt_500MB = pd.merge(exfilt_500MB, _upload_bytes, on=['src_ip', 'dst_ip'], how='inner')

# keep the ones with more than 500MB
exfilt_500MB = exfilt_500MB[exfilt_500MB['up_bytes'] > int(500e6)]

# Add the country and org
exfilt_500MB['dst_cc'] = exfilt_500MB['dst_ip'].apply(lambda x: gi.country_code_by_addr(x))
exfilt_500MB['dst_org'] = exfilt_500MB['dst_ip'].apply(lambda x: gi2.org_by_addr(x))
print("Exfiltration of more than 500MB: \n" + str(exfilt_500MB))

# Plot this exfilt_500MB
exfilt_500MB.plot(x='dst_ip', y='up_bytes', kind='bar', title="Exfiltration of more than 500MB", xlabel="IP", ylabel="Bytes")
plt.savefig('./Graficos/exfiltration_500MB.png')
plt.show()



# Check for periodic communication sending always the same amount of bytes
# Check the number of bytes uploaded per flow for each source IP
normal_upload = data.loc[~(data['dst_ip']).str.startswith('192.168.109.')].groupby(['src_ip'])['up_bytes'].sum().reset_index(name='up_bytes')
normal_flows_upload = data.loc[~(data['dst_ip']).str.startswith('192.168.109.')].groupby(['src_ip']).size().reset_index(name='counts')
normal_upload = pd.merge(normal_upload, normal_flows, on='src_ip', how='inner')
normal_upload['avg_up_bytes'] = normal_upload['up_bytes'] / normal_upload['counts']

# Check the number of bytes uploaded per flow for each source IP - test
test_upload = test.loc[~(test['dst_ip']).str.startswith('192.168.109.')].groupby(['src_ip'])['up_bytes'].sum().reset_index(name='up_bytes')
test_flows_upload = test.loc[~(test['dst_ip']).str.startswith('192.168.109.')].groupby(['src_ip']).size().reset_index(name='counts')
test_upload = pd.merge(test_upload, test_flows, on='src_ip', how='inner')
test_upload['avg_up_bytes'] = test_upload['up_bytes'] / test_upload['counts']

### Country statistics analysis
print("\nCountry statistics analysis\n")

# Number of flows to each country
country_info = data.loc[~(data['dst_ip']).str.startswith('192.168.109.')].groupby(['dst_cc']).size().reset_index(name='counts')
# Number of uploaded and downloaded bytes
country_info['up_bytes'] = data.loc[~(data['dst_ip']).str.startswith('192.168.109.')].groupby(['dst_cc'])['up_bytes'].sum().reset_index(name='up_bytes')['up_bytes']
country_info['down_bytes'] = data.loc[~(data['dst_ip']).str.startswith('192.168.109.')].groupby(['dst_cc'])['down_bytes'].sum().reset_index(name='down_bytes')['down_bytes']

# Plot the top 5 countries with more flows (only, without the bytes)
country_info = country_info.sort_values(by='counts', ascending=False)
country_info = country_info.head(5).set_index('dst_cc')
country_info['counts'].plot.bar(figsize=(20,10), title="Top 5 countries with more flows", xlabel="Country", ylabel="Flows")
plt.savefig('./Graficos/top_5_countries_with_more_flows.png')
plt.show()

country_info = country_info.sort_values(by='up_bytes', ascending=False).head(5)
country_info['up_bytes'].plot.bar(figsize=(20,10), title="Top 5 countries with more uploaded bytes", xlabel="Country", ylabel="Flows")
plt.savefig('./Graficos/top_5_countries_with_more_flows2.png')
plt.show()

country_info = country_info.sort_values(by='down_bytes', ascending=False).head(5)
country_info['down_bytes'].plot.bar(figsize=(20,10), title="Top 5 countries with more downloaded bytes", xlabel="Country", ylabel="Flows")
plt.savefig('./Graficos/top_5_countries_with_more_flows3.png')
plt.show()


# # Plot the top 5 countries with more flows
# country_info = country_info.sort_values(by='counts', ascending=False)
# country_info = country_info.head(5).set_index('dst_cc')
# country_info.plot.bar(figsize=(20,10), title="Top 5 countries with more flows", xlabel="Country", ylabel="Flows")
# plt.savefig('./../plots/top_5_countries_with_more_flows.png')
# plt.show()

# # Plot the top 5 countries with more uploaded bytes
# country_info = country_info.sort_values(by='up_bytes', ascending=False)
# country_info = country_info.head(5).set_index('dst_cc')
# country_info.plot.bar(figsize=(20,10), title="Top 5 countries with more uploaded bytes", xlabel="Country", ylabel="Bytes")
# plt.savefig('./../plots/top_5_countries_with_more_uploaded_bytes.png')
# plt.show()

# # Plot the top 5 countries with more downloaded bytes
# country_info = country_info.sort_values(by='down_bytes', ascending=False)
# country_info = country_info.head(5).set_index('dst_cc')
# country_info.plot.bar(figsize=(20,10), title="Top 5 countries with more downloaded bytes", xlabel="Country", ylabel="Bytes")
# plt.savefig('./../plots/top_5_countries_with_more_downloaded_bytes.png')
# plt.show()



# Make the mean, standard deviation, min and max of the up_bytes and down_bytes
country_info['up_bytes_mean'] = data.loc[~(data['dst_ip']).str.startswith('192.168.109.')].groupby(['dst_cc'])['up_bytes'].mean().reset_index(name='up_bytes_mean')['up_bytes_mean']
# country_info['up_bytes_std'] = data.loc[~(data['dst_ip']).str.startswith('192.168.109.')].groupby(['dst_cc'])['up_bytes'].std().reset_index(name='up_bytes_std')['up_bytes_std']
# country_info['up_bytes_min'] = data.loc[~(data['dst_ip']).str.startswith('192.168.109.')].groupby(['dst_cc'])['up_bytes'].min().reset_index(name='up_bytes_min')['up_bytes_min']
# country_info['up_bytes_max'] = data.loc[~(data['dst_ip']).str.startswith('192.168.109.')].groupby(['dst_cc'])['up_bytes'].max().reset_index(name='up_bytes_max')['up_bytes_max']

# Same for down_bytes
country_info['down_bytes_mean'] = data.loc[~(data['dst_ip']).str.startswith('192.168.109.')].groupby(['dst_cc'])['down_bytes'].mean().reset_index(name='down_bytes_mean')['down_bytes_mean']
# country_info['down_bytes_std'] = data.loc[~(data['dst_ip']).str.startswith('192.168.109.')].groupby(['dst_cc'])['down_bytes'].std().reset_index(name='down_bytes_std')['down_bytes_std']
# country_info['down_bytes_min'] = data.loc[~(data['dst_ip']).str.startswith('192.168.109.')].groupby(['dst_cc'])['down_bytes'].min().reset_index(name='down_bytes_min')['down_bytes_min']
# country_info['down_bytes_max'] = data.loc[~(data['dst_ip']).str.startswith('192.168.109.')].groupby(['dst_cc'])['down_bytes'].max().reset_index(name='down_bytes_max')['down_bytes_max']


# The same for test
country_info_test = test.loc[~(test['dst_ip']).str.startswith('192.168.109.')].groupby(['dst_cc']).size().reset_index(name='counts')
country_info_test['up_bytes'] = test.loc[~(test['dst_ip']).str.startswith('192.168.109.')].groupby(['dst_cc'])['up_bytes'].sum().reset_index(name='up_bytes')['up_bytes']
country_info_test['down_bytes'] = test.loc[~(test['dst_ip']).str.startswith('192.168.109.')].groupby(['dst_cc'])['down_bytes'].sum().reset_index(name='down_bytes')['down_bytes']

country_info_test['up_bytes_mean'] = test.loc[~(test['dst_ip']).str.startswith('192.168.109.')].groupby(['dst_cc'])['up_bytes'].mean().reset_index(name='up_bytes_mean')['up_bytes_mean']
# country_info_test['up_bytes_std'] = test.loc[~(test['dst_ip']).str.startswith('192.168.109.')].groupby(['dst_cc'])['up_bytes'].std().reset_index(name='up_bytes_std')['up_bytes_std']
# country_info_test['up_bytes_min'] = test.loc[~(test['dst_ip']).str.startswith('192.168.109.')].groupby(['dst_cc'])['up_bytes'].min().reset_index(name='up_bytes_min')['up_bytes_min']
# country_info_test['up_bytes_max'] = test.loc[~(test['dst_ip']).str.startswith('192.168.109.')].groupby(['dst_cc'])['up_bytes'].max().reset_index(name='up_bytes_max')['up_bytes_max']

country_info_test['down_bytes_mean'] = test.loc[~(test['dst_ip']).str.startswith('192.168.109.')].groupby(['dst_cc'])['down_bytes'].mean().reset_index(name='down_bytes_mean')['down_bytes_mean']
# country_info_test['down_bytes_std'] = test.loc[~(test['dst_ip']).str.startswith('192.168.109.')].groupby(['dst_cc'])['down_bytes'].std().reset_index(name='down_bytes_std')['down_bytes_std']
# country_info_test['down_bytes_min'] = test.loc[~(test['dst_ip']).str.startswith('192.168.109.')].groupby(['dst_cc'])['down_bytes'].min().reset_index(name='down_bytes_min')['down_bytes_min']
# country_info_test['down_bytes_max'] = test.loc[~(test['dst_ip']).str.startswith('192.168.109.')].groupby(['dst_cc'])['down_bytes'].max().reset_index(name='down_bytes_max')['down_bytes_max']

# Merge data information with test
country_diff = pd.merge(country_info, country_info_test, on='dst_cc', how='right')
country_diff = country_diff.fillna(0)

# Removing the ones that have less than 50 counts_y (test), 
# or have a 50% increase between counts_x (normal) and counts_y (test)
# or have a 20% increase between up_bytes_x (normal) and up_bytes_y (test)
# or have a 20% increase between down_bytes_x (normal) and down_bytes_y (test) 
country_diff = country_diff[((country_diff['counts_y'] > 200) &
                            ((country_diff['counts_y'] > (country_diff['counts_x']*2)) |
                            (country_diff['up_bytes_y'] > (country_diff['up_bytes_x']*1.5)) |
                            (country_diff['down_bytes_y'] > (country_diff['down_bytes_x']*1.5))))]
                        
print("Countries with increase of communication to the exterior: \n" + str(country_diff))

# Plot the statistics of the countries
country_diff_plot = country_diff[['dst_cc', 'counts_x', 'counts_y', 'up_bytes_x', 'up_bytes_y', 'down_bytes_x', 'down_bytes_y']].set_index('dst_cc')
country_diff_plot = country_diff_plot.rename(columns={'counts_x': 'counts_normal', 'counts_y': 'counts_test', 'up_bytes_x': 'up_bytes_normal', 'up_bytes_y': 'up_bytes_test', 'down_bytes_x': 'down_bytes_normal', 'down_bytes_y': 'down_bytes_test'})
ax = country_diff_plot.plot.bar(figsize=(20,10), title="Statistics of the countries (ATTENTION: log scale on y-axis)", xlabel="Countries", ylabel="Counts and Bytes (log scale)", logy=True)
plt.savefig('./Graficos/statistics_of_the_countries_log_scale.png')
plt.show()

### Country statistics 
print("\nCountry statistics\n")

# Mean, Standard Deviation and Variance of the flows to each country
country_info = data.loc[~(data['dst_ip']).str.startswith('192.168.109.')].groupby(['dst_cc']).size().reset_index(name='counts')
country_info['up_bytes_mean'] = data.loc[~(data['dst_ip']).str.startswith('192.168.109.')].groupby(['dst_cc'])['up_bytes'].mean().reset_index(name='up_bytes_mean')['up_bytes_mean']
country_info['up_bytes_std'] = data.loc[~(data['dst_ip']).str.startswith('192.168.109.')].groupby(['dst_cc'])['up_bytes'].std().reset_index(name='up_bytes_std')['up_bytes_std']

# Mean, Standard Deviation and Variance of the flows to each country - test
country_info_test = test.loc[~(test['dst_ip']).str.startswith('192.168.109.')].groupby(['dst_cc']).size().reset_index(name='counts')
country_info_test['up_bytes_mean_test'] = test.loc[~(test['dst_ip']).str.startswith('192.168.109.')].groupby(['dst_cc'])['up_bytes'].mean().reset_index(name='up_bytes_mean')['up_bytes_mean']
country_info_test['up_bytes_std_test'] = test.loc[~(test['dst_ip']).str.startswith('192.168.109.')].groupby(['dst_cc'])['up_bytes'].std().reset_index(name='up_bytes_std')['up_bytes_std']

# Mean, Standard Deviation and Variance of uplodaded bytes per flow to each country
country_info['up_bytes_mean_p_flow'] = data.loc[~(data['dst_ip']).str.startswith('192.168.109.')].groupby(['dst_cc'])['up_bytes'].mean().reset_index(name='up_bytes_mean')['up_bytes_mean']
country_info['up_bytes_std_p_flow'] = data.loc[~(data['dst_ip']).str.startswith('192.168.109.')].groupby(['dst_cc'])['up_bytes'].std().reset_index(name='up_bytes_std')['up_bytes_std']
country_info['up_bytes_var_p_flow'] = data.loc[~(data['dst_ip']).str.startswith('192.168.109.')].groupby(['dst_cc'])['up_bytes'].var().reset_index(name='up_bytes_var')['up_bytes_var']

# Mean, Standard Deviation and Variance of uplodaded bytes per flow to each country - test
country_info_test['up_bytes_mean_p_flow_test'] = test.loc[~(test['dst_ip']).str.startswith('192.168.109.')].groupby(['dst_cc'])['up_bytes'].mean().reset_index(name='up_bytes_mean')['up_bytes_mean']
country_info_test['up_bytes_std_p_flow_test'] = test.loc[~(test['dst_ip']).str.startswith('192.168.109.')].groupby(['dst_cc'])['up_bytes'].std().reset_index(name='up_bytes_std')['up_bytes_std']
country_info_test['up_bytes_var_p_flow_test'] = test.loc[~(test['dst_ip']).str.startswith('192.168.109.')].groupby(['dst_cc'])['up_bytes'].var().reset_index(name='up_bytes_var')['up_bytes_var']

# Mean, Standard Deviation and Variance of downlodaded bytes per flow to each country
country_info['down_bytes_mean_p_flow'] = data.loc[~(data['dst_ip']).str.startswith('192.168.109.')].groupby(['dst_cc'])['down_bytes'].mean().reset_index(name='down_bytes_mean')['down_bytes_mean']
country_info['down_bytes_std_p_flow'] = data.loc[~(data['dst_ip']).str.startswith('192.168.109.')].groupby(['dst_cc'])['down_bytes'].std().reset_index(name='down_bytes_std')['down_bytes_std']
country_info['down_bytes_var_p_flow'] = data.loc[~(data['dst_ip']).str.startswith('192.168.109.')].groupby(['dst_cc'])['down_bytes'].var().reset_index(name='down_bytes_var')['down_bytes_var']

# Mean, Standard Deviation and Variance of downlodaded bytes per flow to each country - test
country_info_test['down_bytes_mean_p_flow_test'] = test.loc[~(test['dst_ip']).str.startswith('192.168.109.')].groupby(['dst_cc'])['down_bytes'].mean().reset_index(name='down_bytes_mean')['down_bytes_mean']
country_info_test['down_bytes_std_p_flow_test'] = test.loc[~(test['dst_ip']).str.startswith('192.168.109.')].groupby(['dst_cc'])['down_bytes'].std().reset_index(name='down_bytes_std')['down_bytes_std']
country_info_test['down_bytes_var_p_flow_test'] = test.loc[~(test['dst_ip']).str.startswith('192.168.109.')].groupby(['dst_cc'])['down_bytes'].var().reset_index(name='down_bytes_var')['down_bytes_var']

### Rule 1 --> Flows increase in 200% 
print("\nRule 1 --> Flows increase in 200% \n")

# Flows and Stats general
normal_stat = data.groupby(['src_ip']).size().reset_index(name='flows')
normal_stat['flows_to_internet'] = data.loc[(data['dst_ip'].apply(lambda x: ipaddress.ip_address(x).is_private)==False)].groupby(['src_ip']).size().reset_index(name='flows_to_internet')['flows_to_internet']
normal_stat['flows_to_private_ips'] = data.loc[(data['dst_ip'].apply(lambda x: ipaddress.ip_address(x).is_private))].groupby(['src_ip']).size().reset_index(name='flows_to_private_ips')['flows_to_private_ips']
normal_stat['flows_to_servers'] = data.loc[(data['dst_ip'].isin(server_ips['index']))].groupby(['src_ip']).size().reset_index(name='flows_to_servers')['flows_to_servers']
normal_stat['total_up_bytes'] = data.groupby(['src_ip'])['up_bytes'].sum().reset_index(name='up_bytes')['up_bytes']
normal_stat['up_bytes_per_flow'] = data.groupby(['src_ip'])['up_bytes'].sum().reset_index(name='up_bytes')['up_bytes']/normal_stat['flows']
normal_stat['total_down_bytes'] = data.groupby(['src_ip'])['down_bytes'].sum().reset_index(name='down_bytes')['down_bytes']
normal_stat['down_bytes_per_flow'] = data.groupby(['src_ip'])['down_bytes'].sum().reset_index(name='down_bytes')['down_bytes']/normal_stat['flows']
normal_stat['% udp_flows'] = data.loc[(data['proto']=='udp')].groupby(['src_ip']).size().reset_index(name='udp_flows')['udp_flows']/normal_stat['flows']
normal_stat['% tcp_flows'] = data.loc[(data['proto']=='tcp')].groupby(['src_ip']).size().reset_index(name='tcp_flows')['tcp_flows']/normal_stat['flows']
normal_stat['up_bytes_per_tcp_flow'] = data.loc[(data['proto']=='tcp')].groupby(['src_ip'])['up_bytes'].sum().reset_index(name='up_bytes')['up_bytes']/normal_stat['% tcp_flows']
normal_stat['up_bytes_per_udp_flow'] = data.loc[(data['proto']=='udp')].groupby(['src_ip'])['up_bytes'].sum().reset_index(name='up_bytes')['up_bytes']/normal_stat['% udp_flows']
normal_stat['down_bytes_per_tcp_flow'] = data.loc[(data['proto']=='tcp')].groupby(['src_ip'])['down_bytes'].sum().reset_index(name='down_bytes')['down_bytes']/normal_stat['% tcp_flows']
normal_stat['down_bytes_per_udp_flow'] = data.loc[(data['proto']=='udp')].groupby(['src_ip'])['down_bytes'].sum().reset_index(name='down_bytes')['down_bytes']/normal_stat['% udp_flows']

mean_flows = normal_stat['flows'].mean()
mean_flows_to_internet = normal_stat['flows_to_internet'].mean()
mean_flows_to_private_ips = normal_stat['flows_to_private_ips'].mean()
mean_flows_to_servers = normal_stat['flows_to_servers'].mean()
mean_total_up_bytes = normal_stat['total_up_bytes'].mean()
mean_up_bytes_per_flow = normal_stat['up_bytes_per_flow'].mean()
mean_total_down_bytes = normal_stat['total_down_bytes'].mean()
mean_down_bytes_per_flow = normal_stat['down_bytes_per_flow'].mean()
mean_perc_udp_flows = normal_stat['% udp_flows'].mean()
mean_perc_tcp_flows = normal_stat['% tcp_flows'].mean()
mean_up_bytes_per_tcp_flow = normal_stat['up_bytes_per_tcp_flow'].mean()
mean_up_bytes_per_udp_flow = normal_stat['up_bytes_per_udp_flow'].mean()
mean_down_bytes_per_tcp_flow = normal_stat['down_bytes_per_tcp_flow'].mean()
mean_down_bytes_per_udp_flow = normal_stat['down_bytes_per_udp_flow'].mean()


# Average server IPs access (server IPs: 234, 239, 224, 228)
avg_server_access = data.loc[(data['dst_ip'].isin(server_ips['index']))].groupby(['src_ip']).size().reset_index(name='counts')
avg_server_access = avg_server_access['counts'].mean()
print("Average server IPs access: \n" + str(int(avg_server_access)))

# check the test flows that access the server IPs (server IPs: 234, 239, 224, 228) with more than 50 flows and 3 times the average
test_server_access = test.loc[(test['dst_ip'].isin(server_ips['index']))].groupby(['src_ip']).size().reset_index(name='counts')
test_server_access = test_server_access[test_server_access['counts'] > (avg_server_access*3)]
print("The IPs that activate an alarm: \n" + test_server_access.to_string(index=False))
# check the test flows that access the server IPs (server IPs: 234, 239, 224, 228) with more than 50 flows and 3 times the average
test_server_access = test.loc[(test['dst_ip'].isin(server_ips['index']))].groupby(['src_ip']).size().reset_index(name='counts')
test_server_access = test_server_access[test_server_access['counts'] > (avg_server_access*5)]

print("The IPs to be blocked: \n" + test_server_access.to_string(index=False))



### Rule 2 --> Internal Communications
print("\nRule 2 --> Internal Communications\n")
# Check for internal comms in normal (private IP to private IP)
normal_internal = data.loc[(data['src_ip'].apply(lambda x: ipaddress.ip_address(x).is_private)) & (data['dst_ip'].apply(lambda x: ipaddress.ip_address(x).is_private))]
normal_internal = normal_internal.groupby(['src_ip', 'dst_ip']).size().reset_index(name='counts')
#print("Internal communications in normal: \n" + str(normal_internal))

# Average count of internal comms
avg_internal = normal_internal['counts'].mean()
print("Average internal communications flows: \n" + str(int(avg_internal)))

# Check for new internal comms in test (private IP to private IP)
test_internal = test.loc[(test['src_ip'].apply(lambda x: ipaddress.ip_address(x).is_private)) & (test['dst_ip'].apply(lambda x: ipaddress.ip_address(x).is_private))]
test_internal = test_internal.groupby(['src_ip', 'dst_ip']).size().reset_index(name='counts')
#print("Internal communications in test: \n" + str(test_internal))

# Get the difference to check the new internal comms in test
internal_diff = pd.merge(normal_internal, test_internal, on=['src_ip', 'dst_ip'], how='right')
internal_diff = internal_diff.fillna(0)
internal_diff = internal_diff[(internal_diff['counts_x'] == 0) & (internal_diff['counts_y'] > 0)]
internal_diff = internal_diff[['src_ip', 'dst_ip', 'counts_y']]
internal_diff = internal_diff.rename(columns={'counts_y': 'counts'})
# print("New internal communications flows: \n" + str(internal_diff))

# Check in the old internal comms if there are any that have more than 3 times the average
internal_diff = pd.merge(normal_internal, test_internal, on=['src_ip', 'dst_ip'], how='right')
internal_diff = internal_diff.fillna(0)
internal_diff = internal_diff[(internal_diff['counts_x'] > (avg_internal*3)) & (internal_diff['counts_y'] > 0)]
internal_diff = internal_diff[['src_ip', 'dst_ip', 'counts_y']]
internal_diff = internal_diff.rename(columns={'counts_y': 'counts'})
print("Alarm on this Internal communications flows: \n" + internal_diff.to_string(index=False))


### Rule 3 --> Naughty Countries
print("\nRule 3 --> Naughty Countries\n")

# Check the average of flows per country
avg_flows_country = data.loc[~(data['dst_ip']).str.startswith('192.168.109.')].groupby(['dst_cc']).size().reset_index(name='counts')
avg_flows_country = avg_flows_country['counts'].mean()
print("Average flows per country: \n" + str(int(avg_flows_country)))

# Check if the test has more than 18 times the average OR is INF of flows per country
test_flows_country = test.loc[~(test['dst_ip']).str.startswith('192.168.109.')].groupby(['dst_cc']).size().reset_index(name='counts')
test_flows_country = test_flows_country[(test_flows_country['counts'] > (avg_flows_country*18)) | (test_flows_country['counts'] == float('inf'))]
print("The IPs that activate an alarm: \n" + test_flows_country.to_string(index=False))

# Check for countries that exist in test but not in normal
test_flows_country = test.loc[~(test['dst_ip']).str.startswith('192.168.109.')].groupby(['dst_cc']).size().reset_index(name='counts')
test_flows_country = test_flows_country[~(test_flows_country['dst_cc'].isin(data.loc[~(data['dst_ip']).str.startswith('192.168.109.')].groupby(['dst_cc']).size().reset_index(name='counts')['dst_cc']))]

# Check which of those countries have more than 500 flows
test_flows_country = test_flows_country[test_flows_country['counts'] > 500]
print("The IPs to be blocked: \n" + test_flows_country.to_string(index=False))


### Rule 4 --> Upload Volumes
print("\nRule 4 --> Upload Volumes\n")

# Average upload bytes in normal
avg_up_bytes = data.groupby(['src_ip'])['up_bytes'].sum().reset_index(name='up_bytes')['up_bytes'].mean()
print("Average upload bytes: \n" + str((avg_up_bytes)))

# Check if there are flows in test that pass 3 times the average upload bytes 
test_up_bytes = test.groupby(['src_ip'])['up_bytes'].sum().reset_index(name='up_bytes')
test_up_bytes = test_up_bytes[test_up_bytes['up_bytes'] > (avg_up_bytes*3)]
print("The IPs that activate an alarm: \n" + test_up_bytes.to_string(index=False))

# Check if there are flows in test that pass 5 times the average upload bytes 
test_up_bytes = test.groupby(['src_ip'])['up_bytes'].sum().reset_index(name='up_bytes')
test_up_bytes = test_up_bytes[test_up_bytes['up_bytes'] > (avg_up_bytes*5)]
print("The IPs to be blocked: \n" + test_up_bytes.to_string(index=False))


### Rule 5 --> New protocols or More frequent protocols
print("\nRule 5 --> New protocols or More frequent protocols\n")

# Check the used protocols
normal_protocols = data.groupby(['proto']).size().reset_index(name='counts')
normal_protocols['%'] = normal_protocols['counts']/normal_protocols['counts'].sum()
print("Protocols in normal: \n" + normal_protocols.to_string(index=False))

# Check if there's a new protocol in test
test_protocols = test.groupby(['proto']).size().reset_index(name='counts')
test_protocols['%'] = test_protocols['counts']/test_protocols['counts'].sum()
test_protocols = pd.merge(normal_protocols, test_protocols, on='proto', how='right')
test_protocols = test_protocols.fillna(0)
test_protocols = test_protocols[(test_protocols['counts_x'] == 0) & (test_protocols['counts_y'] > 0)]
test_protocols = test_protocols[['proto', 'counts_y', '%_y']]
test_protocols = test_protocols.rename(columns={'counts_y': 'counts', '%_y': '%'})
print("[ALARM] New protocols in test: \n" + test_protocols.to_string(index=False))

### Rule 6 --> New ports (check port scanning)
print("\nRule 6 --> New ports (check port scanning)\n")

# Check the used ports
normal_ports = data.groupby(['port']).size().reset_index(name='counts')
normal_ports['%'] = normal_ports['counts']/normal_ports['counts'].sum()
print("Ports in normal: \n" + normal_ports.to_string(index=False))

# Check if there's a new port in test
test_ports = test.groupby(['port']).size().reset_index(name='counts')
test_ports['%'] = test_ports['counts']/test_ports['counts'].sum()
test_ports = pd.merge(normal_ports, test_ports, on='port', how='right')
test_ports = test_ports.fillna(0)
test_ports = test_ports[(test_ports['counts_x'] == 0) & (test_ports['counts_y'] > 0)]
test_ports = test_ports[['port', 'counts_y', '%_y']]
test_ports = test_ports.rename(columns={'counts_y': 'counts', '%_y': '%'})
print("[ALARM] New ports in test: \n" + test_ports.to_string(index=False))











