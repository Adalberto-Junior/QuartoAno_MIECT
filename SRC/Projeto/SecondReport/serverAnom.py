import pandas as pd
import numpy as np
import ipaddress
import dns.resolver
import dns.reversename
import pygeoip
import matplotlib.pyplot as plt 
from matplotlib import gridspec

serverfile='servers9.parquet'

### Read parquet server files
server=pd.read_parquet(serverfile)

### IP geolocalization
gi=pygeoip.GeoIP('./GeoIP.dat')
gi2=pygeoip.GeoIP('./GeoIPASNum.dat')

# Check what protocols were used
protocols = server['proto'].unique()
print("\nProtocols used: " + str(protocols))
server['proto'].value_counts().plot(kind='bar')
plt.title('Protocols used and their frequency')
plt.xlabel('Protocol')
plt.ylabel('Occurences')
plt.show()

# Check what protocols were used
port = server['port'].unique()
print("\nPort used: " + str(port))
server['port'].value_counts().plot(kind='bar')
plt.title('Port used and their frequency')
plt.xlabel('Port')
plt.ylabel('Occurences')
plt.show()

#Number of upload and download packets/bytes
print("\nUploads: " + str(server['up_bytes'].describe()))
print("\nDownloads: " + str(server['down_bytes'].describe()))

#source IP addresses
source_ips = server['src_ip'].unique()
#print("source IPs used: " + str(source_ips))
print("\nTotal source IP: " + str(len(source_ips)))

#source IP addresses
dst_ips = server['dst_ip'].unique()
#print("source IPs used: " + str(source_ips))
print("\nTotal destination IP: " + str(len(dst_ips)))

# Verify that port 53 only has UDP connections
tcp53 = server.loc[(server['port'] == 53) & (server['proto'] != 'udp')]
if tcp53.empty:
    print("\nNo TCP connections on port 53\n")
    udpF=server.loc[server['proto']=='udp']
    print("udpFlow:\n" + str(udpF))
    #Number of UDP flows for each source IP
    nudpF=server.loc[server['proto']=='udp'].groupby(['src_ip'])['up_bytes'].count()
    print("\nnudpF: \n" + str(nudpF))
else:
    print("TCP connections on port 53: " + str(tcp53))

# Verify that port 443 only has TCP connections
udp443 = server.loc[(server['port'] == 443) & (server['proto'] != 'tcp')]
if udp443.empty:
    print("\nNo UDP connections on port 443\n")
else:
    print("\nUDP connections on port 443:\n " + str(udp443))
    
 #Number of tcp flows to port 443, for each source IP
nutcpF443=server.loc[(server['proto']=='tcp')&(server['port']==443)].groupby(['src_ip'])['up_bytes'].count()
print("\nnutcpF443: \n" + str(nutcpF443))

# Add the country code and the organization to flow server
server['dst_cc'] = server['dst_ip'].apply(lambda x: gi.country_code_by_addr(x))
server['dst_org'] = server['dst_ip'].apply(lambda x: gi2.org_by_addr(x))

# Check the connections to each country
countries = server['dst_cc'].unique()
print("\nCountries: \n" + str(countries))

# Check the connections to each organization
organizations = server['dst_org'].unique()
print("\nOrganizations: \n" + str(organizations))

#Is destination IPv4 a public address?
NET=ipaddress.IPv4Network('82.155.129.0/24')
bpublic=server.apply(lambda x: ipaddress.IPv4Address(x['dst_ip']) not in NET,axis=1)

#Analysis of the non-anomalous behaviors:
print("\n### Analysis of the non-anomalous behaviors: #####################################\n")
print("\n### Internal Communications Analysis #####################################\n")
# Flows from internal IPs to internal IPs (82.155.129.X)
internal_flows = server.loc[(server['src_ip'].str.startswith('82.155.129.')) & (server['dst_ip'].str.startswith('82.155.129.'))]

# Number of UDP flows for each source IP (internal)
nudpF_internal = internal_flows.loc[internal_flows['proto']=='udp'].groupby(['src_ip']).count()
print("\nNumber of UDP flows for each source IP: \n" + str(nudpF_internal))

# Number of TCP flows for each source IP (internal)
ntcpF_internal = internal_flows.loc[internal_flows['proto']=='tcp'].groupby(['src_ip']).count()
print("\nNumber of TCP flows for each source IP counting upload bytes: \n" + str(ntcpF_internal))

# Verify which IPs belong to servers (IPs that are private (start with 82.155.129.X) that have more communication, both as source or destination)
src_ips = server.loc[server['src_ip'].str.startswith('82.155.129.')].groupby('src_ip').size().reset_index(name='counts')
dst_ips = server.loc[server['dst_ip'].str.startswith('82.155.129.')].groupby('dst_ip').size().reset_index(name='counts')

# Check what's the average value of the counts
print("\nAverage value of the counts: \n" + str(src_ips['counts'].mean()))

# Combine the counts
server_ips = src_ips.set_index('src_ip').add(dst_ips.set_index('dst_ip'), fill_value=0).reset_index().sort_values(by='counts', ascending=False).reset_index(drop=True)

print("\nServer IPs: \n" + str(server_ips))

# Average value of the counts without the top 4 IPs
print("\nAverage value of the counts without the top 4 IPs: \n" + str(server_ips['counts'][4:].mean()))



# Plot the top 8 server IPs
server_ips.head(8).plot(kind='bar', x='src_ip', y='counts')
plt.title('Potential server IPs')
plt.xlabel('IP')
plt.ylabel('Flows')
plt.savefig('./Graficos/top8_potential_server_ips.png')
plt.show()

##
# Number of UDP flow to port 53 (DNS), for each source IP (internal flows)
nudpF53_internal = internal_flows.loc[(internal_flows['proto']=='udp')&(internal_flows['port']==53)].groupby(['src_ip']).count()
print("\nNumber of UDP flow to port 53 (DNS), for each source IP, using UDP: " + str(nudpF53_internal))

# Total uploaded bytes to destination port 53 using UDP, for each source IP (internal flows)
upS_internal = internal_flows.loc[((internal_flows['proto']=='udp')&(internal_flows['port']==53))].groupby(['src_ip'])['up_bytes'].sum()
print("\nTotal uploaded bytes to destination port 53 using UDP, for each source IP: " + str(upS_internal))

##
# Number of TCP flows to port 443, for each source IP (internal flows)
ntcpF443_internal = internal_flows.loc[(internal_flows['proto']=='tcp')&(internal_flows['port']==443)].groupby(['src_ip']).count()
print("\nNumber of TCP flows to port 443 (HTTPS), for each source IP, using TCP: " + str(ntcpF443_internal))

# Total uploaded bytes to destination port 443 using TCP, for each source IP (internal flows)
upS_internal = internal_flows.loc[((internal_flows['port']==443))].groupby(['src_ip'])['up_bytes'].sum()
print("\nTotal uploaded bytes to destination port 443, for each source IP: " + str(upS_internal))


##
# Uploaded bytes per flow, for each source IP (internal flows)
upF_internal = internal_flows.groupby(['src_ip'])['up_bytes'].sum()
print("\nUploaded bytes per flow, for each source IP: \n" + str(upF_internal))

# Downloaded bytes per flow, for each source IP (internal flows)
downS_internal = internal_flows.groupby(['src_ip'])['down_bytes'].sum()
print("\nDownloaded bytes per flow, for each source IP: \n" + str(downS_internal))


##
# Normal timestamps per source IP flows
ts_internal = internal_flows.groupby(['src_ip'])['timestamp'].mean()
print("\nNormal timestamps per source IP flows: \n" + str(ts_internal))

# Check flows per country (internal flows)
cc_internal = internal_flows.groupby(['dst_cc']).count()
print("\nFlows per country: \n" + str(cc_internal))

# Check flows per organization (internal flows)
org_internal = internal_flows.groupby(['dst_org']).count()
print("\nFlows per organization: \n" + str(org_internal))

### Internal communications statistics

# Mean, Standard Deviation and Variance of the flows to private IPs
print("\n\nMean of the flows to private IPs: " + str(internal_flows['dst_ip'].apply(lambda x: ipaddress.ip_address(x).is_private).mean()))
print("Standard Deviation of the flows to private IPs: " + str(internal_flows['dst_ip'].apply(lambda x: ipaddress.ip_address(x).is_private).std()))
print("Variance of the flows to private IPs: " + str(internal_flows['dst_ip'].apply(lambda x: ipaddress.ip_address(x).is_private).var()))

# Mean, Standard Deviation and Variance of the uploaded bytes per flow to private IPs
print("Mean of the uploaded bytes to private IPs: " + str(internal_flows.loc[(internal_flows['dst_ip'].apply(lambda x: ipaddress.ip_address(x).is_private))]['up_bytes'].mean()))
print("Standard Deviation of the uploaded bytes to private IPs: " + str(internal_flows.loc[(internal_flows['dst_ip'].apply(lambda x: ipaddress.ip_address(x).is_private))]['up_bytes'].std()))
print("Variance of the uploaded bytes to private IPs: " + str(internal_flows.loc[(internal_flows['dst_ip'].apply(lambda x: ipaddress.ip_address(x).is_private))]['up_bytes'].var()))

# Mean, Standard Deviation and Variance of the total uploaded bytes to private IPs
print("Mean of the total uploaded bytes to private IPs: " + str(internal_flows.loc[(internal_flows['dst_ip'].apply(lambda x: ipaddress.ip_address(x).is_private))]['up_bytes'].sum()))
print("Standard Deviation of the total uploaded bytes to private IPs: " + str(internal_flows.loc[(internal_flows['dst_ip'].apply(lambda x: ipaddress.ip_address(x).is_private))]['up_bytes'].sum()))
print("Variance of the total uploaded bytes to private IPs: " + str(internal_flows.loc[(internal_flows['dst_ip'].apply(lambda x: ipaddress.ip_address(x).is_private))]['up_bytes'].sum()))

# Mean, Standard Deviation and Variance of the downloaded bytes per flow to private IPs
print("Mean of the downloaded bytes to private IPs: " + str(internal_flows.loc[(internal_flows['dst_ip'].apply(lambda x: ipaddress.ip_address(x).is_private))]['down_bytes'].mean()))
print("Standard Deviation of the downloaded bytes to private IPs: " + str(internal_flows.loc[(internal_flows['dst_ip'].apply(lambda x: ipaddress.ip_address(x).is_private))]['down_bytes'].std()))
print("Variance of the downloaded bytes to private IPs: " + str(internal_flows.loc[(internal_flows['dst_ip'].apply(lambda x: ipaddress.ip_address(x).is_private))]['down_bytes'].var()))

# Mean, Standard Deviation and Variance of the total downloaded bytes to private IPs
print("Mean of the total downloaded bytes to private IPs: " + str(internal_flows.loc[(internal_flows['dst_ip'].apply(lambda x: ipaddress.ip_address(x).is_private))]['down_bytes'].sum()))
print("Standard Deviation of the total downloaded bytes to private IPs: " + str(internal_flows.loc[(internal_flows['dst_ip'].apply(lambda x: ipaddress.ip_address(x).is_private))]['down_bytes'].sum()))
print("Variance of the total downloaded bytes to private IPs: " + str(internal_flows.loc[(internal_flows['dst_ip'].apply(lambda x: ipaddress.ip_address(x).is_private))]['down_bytes'].sum()))

# Mean, Standard Deviation and Variance of UDP flows to private IPs
print("Mean of the UDP flows to private IPs: " + str(internal_flows.loc[(internal_flows['dst_ip'].apply(lambda x: ipaddress.ip_address(x).is_private))&(internal_flows['proto']=='udp')].groupby(['src_ip']).count().mean()))
print("Standard Deviation of the UDP flows to private IPs: " + str(internal_flows.loc[(internal_flows['dst_ip'].apply(lambda x: ipaddress.ip_address(x).is_private))&(internal_flows['proto']=='udp')].groupby(['src_ip']).count().std()))
print("Variance of the UDP flows to private IPs: " + str(internal_flows.loc[(internal_flows['dst_ip'].apply(lambda x: ipaddress.ip_address(x).is_private))&(internal_flows['proto']=='udp')].groupby(['src_ip']).count().var()))

# Mean, Standard Deviation and Variance of TCP flows to private IPs
print("Mean of the TCP flows to private IPs: " + str(internal_flows.loc[(internal_flows['dst_ip'].apply(lambda x: ipaddress.ip_address(x).is_private))&(internal_flows['proto']=='tcp')].groupby(['src_ip']).count().mean()))
print("Standard Deviation of the TCP flows to private IPs: " + str(internal_flows.loc[(internal_flows['dst_ip'].apply(lambda x: ipaddress.ip_address(x).is_private))&(internal_flows['proto']=='tcp')].groupby(['src_ip']).count().std()))
print("Variance of the TCP flows to private IPs: " + str(internal_flows.loc[(internal_flows['dst_ip'].apply(lambda x: ipaddress.ip_address(x).is_private))&(internal_flows['proto']=='tcp')].groupby(['src_ip']).count().var()))

### External Communications Analysis

print("\n### External Communications Analysis #####################################\n")

# Flows from external IPs to external IPs (82.155.129.X)
external_flows = server.loc[(server['src_ip'].str.startswith('82.155.129.')) & (~server['dst_ip'].str.startswith('82.155.129.'))]

# Number of UDP flows for each source IP (external flows)
nudpF_external = external_flows.loc[external_flows['proto']=='udp'].groupby(['src_ip']).count()
print("\nNumber of UDP flows for each source IP: \n" + str(nudpF_external))

# Number of TCP flows for each source IP (external flows)
ntcpF_external = external_flows.loc[external_flows['proto']=='tcp'].groupby(['src_ip']).count()
print("\nNumber of TCP flows for each source IP counting upload bytes: \n" + str(ntcpF_external))


##
# Number of UDP flow to port 53 (DNS), for each source IP (external flows)
nudpF53_external = external_flows.loc[(external_flows['proto']=='udp')&(external_flows['port']==53)].groupby(['src_ip']).count()
print("\nNumber of UDP flow to port 53 (DNS), for each source IP, using UDP: \n" + str(nudpF53_external))

# Total uploaded bytes to destination port 53 using UDP, for each source IP (external flows)
upS_external = external_flows.loc[((external_flows['proto']=='udp')&(external_flows['port']==53))].groupby(['src_ip'])['up_bytes'].sum()
print("\nTotal uploaded bytes to destination port 53 using UDP, for each source IP: \n" + str(upS_external))


##
# Number of TCP flows to port 443, for each source IP (external flows)
ntcpF443_external = external_flows.loc[(external_flows['proto']=='tcp')&(external_flows['port']==443)].groupby(['src_ip']).count()
print("\nNumber of TCP flows to port 443 (HTTPS), for each source IP, using TCP: \n" + str(ntcpF443_external))

# Total uploaded bytes to destination port 443 using TCP, for each source IP (external flows)
upS_external = external_flows.loc[((external_flows['port']==443))].groupby(['src_ip'])['up_bytes'].sum()
print("\nTotal uploaded bytes to destination port 443, for each source IP: \n" + str(upS_external))


##
# Uploaded bytes per flow, for each source IP (external flows)
upF_external = external_flows.groupby(['src_ip'])['up_bytes'].sum()
print("\nUploaded bytes per flow, for each source IP: \n" + str(upF_external))

# Downloaded bytes per flow, for each source IP (external flows)
downS_external = external_flows.groupby(['src_ip'])['down_bytes'].sum()
print("\nDownloaded bytes per flow, for each source IP: \n" + str(downS_external))


##
# Normal timestamps per source IP flows
ts_external = external_flows.groupby(['src_ip'])['timestamp'].mean()
print("\nNormal timestamps per source IP flows: \n" + str(ts_external))

# Check flows per country (external flows)
cc_external = external_flows.groupby(['dst_cc']).count()
print("\nFlows per country: \n" + str(cc_external))

# Check flows per organization (external flows)
org_external = external_flows.groupby(['dst_org']).count()
print("\nFlows per organization: \n" + str(org_external))
print("\n\n")
# Mean, Standard Deviation and Variance of the flows to public IPs
print("\nMean of the flows to public IPs: " + str(external_flows['dst_ip'].apply(lambda x: ipaddress.ip_address(x).is_private).mean()))
print("\nStandard Deviation of the flows to public IPs: " + str(external_flows['dst_ip'].apply(lambda x: ipaddress.ip_address(x).is_private).std()))
print("\nVariance of the flows to public IPs: " + str(external_flows['dst_ip'].apply(lambda x: ipaddress.ip_address(x).is_private).var()))

# Mean, Standard Deviation and Variance of the uploaded bytes per flow to public IPs
print("\nMean of the uploaded bytes to public IPs: " + str(external_flows.loc[(external_flows['dst_ip'].apply(lambda x: ipaddress.ip_address(x).is_private))]['up_bytes'].mean()))
print("\nStandard Deviation of the uploaded bytes to public IPs: " + str(external_flows.loc[(external_flows['dst_ip'].apply(lambda x: ipaddress.ip_address(x).is_private))]['up_bytes'].std()))
print("\nVariance of the uploaded bytes to public IPs: " + str(external_flows.loc[(external_flows['dst_ip'].apply(lambda x: ipaddress.ip_address(x).is_private))]['up_bytes'].var()))

# Mean, Standard Deviation and Variance of the total uploaded bytes to public IPs
print("\nMean of the total uploaded bytes to public IPs: " + str(external_flows.loc[(external_flows['dst_ip'].apply(lambda x: ipaddress.ip_address(x).is_private))]['up_bytes'].sum()))
print("\nStandard Deviation of the total uploaded bytes to public IPs: " + str(external_flows.loc[(external_flows['dst_ip'].apply(lambda x: ipaddress.ip_address(x).is_private))]['up_bytes'].sum()))
print("\nVariance of the total uploaded bytes to public IPs: " + str(external_flows.loc[(external_flows['dst_ip'].apply(lambda x: ipaddress.ip_address(x).is_private))]['up_bytes'].sum()))

# Mean, Standard Deviation and Variance of the downloaded bytes per flow to public IPs
print("\nMean of the downloaded bytes to public IPs: " + str(external_flows.loc[(external_flows['dst_ip'].apply(lambda x: ipaddress.ip_address(x).is_private))]['down_bytes'].mean()))
print("\nStandard Deviation of the downloaded bytes to public IPs: " + str(external_flows.loc[(external_flows['dst_ip'].apply(lambda x: ipaddress.ip_address(x).is_private))]['down_bytes'].std()))
print("\nVariance of the downloaded bytes to public IPs: " + str(external_flows.loc[(external_flows['dst_ip'].apply(lambda x: ipaddress.ip_address(x).is_private))]['down_bytes'].var()))

# Mean, Standard Deviation and Variance of the total downloaded bytes to public IPs
print("\nMean of the total downloaded bytes to public IPs: " + str(external_flows.loc[(external_flows['dst_ip'].apply(lambda x: ipaddress.ip_address(x).is_private))]['down_bytes'].sum()))
print("\nStandard Deviation of the total downloaded bytes to public IPs: " + str(external_flows.loc[(external_flows['dst_ip'].apply(lambda x: ipaddress.ip_address(x).is_private))]['down_bytes'].sum()))
print("\nVariance of the total downloaded bytes to public IPs: " + str(external_flows.loc[(external_flows['dst_ip'].apply(lambda x: ipaddress.ip_address(x).is_private))]['down_bytes'].sum()))

# Mean, Standard Deviation and Variance of UDP flows to public IPs
print("\nMean of the UDP flows to public IPs: " + str(external_flows.loc[(external_flows['dst_ip'].apply(lambda x: ipaddress.ip_address(x).is_private))&(external_flows['proto']=='udp')].groupby(['src_ip']).count().mean()))
print("\nStandard Deviation of the UDP flows to public IPs: " + str(external_flows.loc[(external_flows['dst_ip'].apply(lambda x: ipaddress.ip_address(x).is_private))&(external_flows['proto']=='udp')].groupby(['src_ip']).count().std()))
print("\nVariance of the UDP flows to public IPs: " + str(external_flows.loc[(external_flows['dst_ip'].apply(lambda x: ipaddress.ip_address(x).is_private))&(external_flows['proto']=='udp')].groupby(['src_ip']).count().var()))

# Mean, Standard Deviation and Variance of TCP flows to public IPs
print("\nMean of the TCP flows to public IPs: " + str(external_flows.loc[(external_flows['dst_ip'].apply(lambda x: ipaddress.ip_address(x).is_private))&(external_flows['proto']=='tcp')].groupby(['src_ip']).count().mean()))
print("\nStandard Deviation of the TCP flows to public IPs: " + str(external_flows.loc[(external_flows['dst_ip'].apply(lambda x: ipaddress.ip_address(x).is_private))&(external_flows['proto']=='tcp')].groupby(['src_ip']).count().std()))
print("\nVariance of the TCP flows to public IPs: " + str(external_flows.loc[(external_flows['dst_ip'].apply(lambda x: ipaddress.ip_address(x).is_private))&(external_flows['proto']=='tcp')].groupby(['src_ip']).count().var()))

