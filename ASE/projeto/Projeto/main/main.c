#include <stdio.h>
#include "driver/i2c.h"
#include "driver/ledc.h"
#include <stdbool.h>
#include <stdlib.h>
#include "spi_flash_mmap.h"
#include <esp_http_server.h>
#include <lwip/sockets.h>
#include <lwip/sys.h>
#include <lwip/api.h>
#include <lwip/netdb.h>
#include "esp_wifi.h"
#include "esp_event.h"
#include "freertos/event_groups.h"
#include "esp_netif.h"
#include "esp_spiffs.h"
#include "TempSensorTC74.h"
#include "esp_timer.h"
#include "driver/gpio.h"
#include "freertos/FreeRTOS.h"
#include "freertos/task.h"
#include "driver/rtc_io.h"
#include "nvs_flash.h"
#include "nvs.h"
#include "esp_log.h"
//#include "esp_err.h"
#include <time.h>
#include <sys/time.h>
#include "sdkconfig.h"
#include "soc/soc_caps.h"
#include "esp_sleep.h"
#include "esp_system.h"
#include <string.h>
#include <sys/unistd.h>
#include <sys/stat.h>
#include "esp_vfs_fat.h"
#include "sdmmc_cmd.h"
//#include "sd_card.h"
#include <time.h>
#include "esp_sntp.h"
//#include <iostream>
//#include <string>

#define EXAMPLE_MAX_CHAR_SIZE    1024

#define ESP_WIFI_SSID "OpenLab"  // WiFi SSID
#define ESP_WIFI_PASSWORD "open.LAB" // WiFi Password
#define ESP_MAXIMUM_RETRY 5

#define WIFI_CONNECTED_BIT BIT0
#define WIFI_FAIL_BIT BIT1

static const char *TAG = "TC74"; // Tag for logs

static int s_retry_num = 0;
int wifi_connect_status = 0; // WiFi connection status


#define MOUNT_POINT "/sdcard"

#define PIN_NUM_MISO  CONFIG_EXAMPLE_PIN_MISO
#define PIN_NUM_MOSI  CONFIG_EXAMPLE_PIN_MOSI
#define PIN_NUM_CLK   CONFIG_EXAMPLE_PIN_CLK
#define PIN_NUM_CS    CONFIG_EXAMPLE_PIN_CS

#define SDA 0
#define SCL 10
#define HOT 50
#define NORMAL 30

// Definition of temperature history size
#define HISTORICAL_SIZE 60
uint8_t historical_temperatures[HISTORICAL_SIZE];
int historical_index = 0;

// Variavel global para saber se o sensor esta a dormir ou nao para poder ligar/desligar os leds?
bool sensor_sleeping = false;


// HTML page to be sent to the client
char html_page[] = "<!DOCTYPE HTML><html>\n"
                   "<head>\n"
                   "  <title>Adalberto / Nuno TC74</title>\n"
                   "  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\n"
                   "  <link rel=\"stylesheet\" href=\"https://use.fontawesome.com/releases/v5.7.2/css/all.css\" integrity=\"sha384-fnmOCqbTlWIlj8LyTjo7mOUStjsKC4pOpQbqyi7RrhN7udi9RwhKkMHpvLbHG9Sr\" crossorigin=\"anonymous\">\n"
                   "  <link rel=\"icon\" href=\"data:,\">\n"
                   "  <style>\n"
                   "    html {font-family: Courier; display: inline-block; text-align: center;}\n"
                   "    p { font-size: 1.2rem; }\n"
                   "    body { margin: 0; }\n"
                   "    .topnav { overflow: hidden; background-color: #008080; color: white; font-size: 1.6rem; }\n"
                   "    .content { padding: 20px; }\n"
                   "    .card { background-color: white; box-shadow: 2px 2px 12px 1px rgba(0,128,128,1); }\n"
                   "    .cards { max-width: 650px; margin: 0 auto; display: grid; grid-gap: 2rem; grid-template-columns: repeat(auto-fit, minmax(300px, 1fr)); }\n"
                   "    .reading { font-size: 2.8rem; }\n"
                   "    .card.temperatureC { color: #008080; }\n"
                   "    button { font-size: 1.2rem; padding: 10px 20px; color: white; background-color: #008080; border: none; border-radius: 5px; cursor: pointer; }\n"
                   "    .button-container { display: flex; justify-content: center; gap: 10px; margin-top: 20px; }\n"
                   "  </style>\n"
                   "  <script src=\"https://cdn.jsdelivr.net/npm/chart.js\"></script>\n"
                   "  <script>\n"
                   "    let tempChart;\n"
                   "    function increaseTemperature() {\n"
                   "      fetch(`/set_temperature?temp=${parseInt(document.getElementById('tempValue').innerText) + 10}`)\n"
                   "        .then(response => response.text())\n"
                   "        .then(data => {\n"
                   "          console.log(data);\n"
                   "        });\n"
                   "    }\n"
                   "    function fetchTemperature() {\n"
                   "      fetch('/')\n"
                   "        .then(response => response.text())\n"
                   "        .then(html => {\n"
                   "          const parser = new DOMParser();\n"
                   "          const doc = parser.parseFromString(html, 'text/html');\n"
                   "          const tempValue = doc.getElementById('tempValue').innerText;\n"
                   "          document.getElementById('tempValue').innerText = tempValue;\n"
                   "        });\n"
                   "    }\n"
                   "    function fetchTemperatureHistory() {\n"
                   "      fetch('/temperature_history')\n"
                   "        .then(response => response.json())\n"
                   "        .then(data => {\n"
                   "          const ctx = document.getElementById('tempChart').getContext('2d');\n"
                   "          if (!tempChart) {\n"
                   "            tempChart = new Chart(ctx, {\n"
                   "              type: 'line',\n"
                   "              data: {\n"
                   "                labels: Array.from({ length: data.length }, (_, i) => -i),\n"
                   "                datasets: [{\n"
                   "                  label: 'Temperature',\n"
                   "                  data: data,\n"
                   "                  borderColor: 'rgba(0, 128, 128, 1)',\n"
                   "                  borderWidth: 1,\n"
                   "                  fill: false\n"
                   "                }]\n"
                   "              },\n"
                   "              options: {\n"
                   "                scales: {\n"
                   "                  x: {\n"
                   "                    display: true,\n"
                   "                    title: {\n"
                   "                      display: true,\n"
                   "                      text: 'Time (seconds ago)'\n"
                   "                    }\n"
                   "                  },\n"
                   "                  y: {\n"
                   "                    display: true,\n"
                   "                    title: {\n"
                   "                      display: true,\n"
                   "                      text: 'Temperature (C)'\n"
                   "                    }\n"
                   "                  }\n"
                   "                }\n"
                   "              }\n"
                   "            });\n"
                   "          } else {\n"
                   "            tempChart.data.labels = Array.from({ length: data.length }, (_, i) => -i);\n"
                   "            tempChart.data.datasets[0].data = data;\n"
                   "            tempChart.update();\n"
                   "          }\n"
                   "        });\n"
                   "    }\n"
                   "    function sleepSensor() {\n"
                   "      fetch('/sleep')\n"
                   "        .then(response => response.text())\n"
                   "        .then(data => {\n"
                   "          console.log(data);\n"
                   "        });\n"
                   "    }\n"
                   "    function wakeupSensor() {\n"
                   "      fetch('/wakeup')\n"
                   "        .then(response => response.text())\n"
                   "        .then(data => {\n"
                   "          console.log(data);\n"
                   "        });\n"
                   "    }\n"
                   "    setInterval(() => {\n"
                   "      fetchTemperature();\n"
                   "      fetchTemperatureHistory();\n"
                   "    }, 1000);\n"
                   "    window.onload = fetchTemperatureHistory;\n"
                   "  </script>\n"
                   "</head>\n"
                   "<body>\n"
                   "  <div class=\"topnav\">\n"
                   "    <h3>Adalberto / Nuno TC74</h3>\n"
                   "  </div>\n"
                   "  <div class=\"content\">\n"
                   "    <div class=\"cards\">\n"
                   "      <div class=\"card temperatureC\">\n"
                   "        <h4><i class=\"fas fa-fan\"></i> Sensor Temperatura</h4>\n"
                   "        <p><span class=\"reading\" id=\"tempValue\">%d</span>&deg;C</p>\n"
                   "      </div>\n"
                   "    </div>\n"
                   "    <button onclick=\"increaseTemperature()\">Increase Temperature by 10&deg;C</button>\n"
                   "    <div class=\"button-container\">\n"
                   "      <button onclick=\"sleepSensor()\">Sleep</button>\n"
                   "      <button onclick=\"wakeupSensor()\">Wake Up</button>\n"
                   "    </div>\n"
                   "    <canvas id=\"tempChart\" width=\"300\" height=\"100\"></canvas>\n"
                   "  </div>\n"
                   "</body>\n"
                   "</html>";



// WiFi Event
static EventGroupHandle_t s_wifi_event_group;

/*Variavel Global: */
 uint8_t temperature[1]; // External variable for temperature
//uint8_t temperature[1]; // Temperature value
uint8_t past_temperature[1]; // Past Temperature value
short int timer = 0; // Timer value
char strftime_buf[64]; // Current Data
const char *file_temp = MOUNT_POINT"/log.txt";
sdmmc_card_t *card;
const char mount_point[] = MOUNT_POINT;
sdmmc_host_t host = SDSPI_HOST_DEFAULT();
char data[EXAMPLE_MAX_CHAR_SIZE];
i2c_master_bus_handle_t busHandle;
i2c_master_dev_handle_t sensorHandle;



const gpio_num_t segmentPins[4] = {
    GPIO_NUM_2, // Luz verde
    GPIO_NUM_3, // Luz laranja
    GPIO_NUM_4, // 
    GPIO_NUM_5, // Luz vermelha
};

// GPIO configuration for LEDs
static void configure_gpio(void)
{
    for (int i = 0; i < 4; i++)
    {
        gpio_reset_pin(segmentPins[i]);
        gpio_set_direction(segmentPins[i], GPIO_MODE_OUTPUT);
    }
}


// Update LEDs based on temperature
void update_leds(void)
{
    if (temperature[0] < NORMAL)
    {
        gpio_set_level(GPIO_NUM_2, 1);
        gpio_set_level(GPIO_NUM_3, 0);
        gpio_set_level(GPIO_NUM_5, 0);
    }
    else if (temperature[0] >= NORMAL && temperature[0] < HOT)
    {
        gpio_set_level(GPIO_NUM_2, 0);
        gpio_set_level(GPIO_NUM_3, 1);
        gpio_set_level(GPIO_NUM_5, 0);
    }
    else
    {
        gpio_set_level(GPIO_NUM_2, 0);
        gpio_set_level(GPIO_NUM_3, 0);
        gpio_set_level(GPIO_NUM_5, 1);
    }
}

// Turn off LEDs
static void turn_off_leds(void)
{
    for (int i = 0; i < 4; i++)
    {
        gpio_set_level(segmentPins[i], 0);
    }
}


// WiFi Event Handler
static void event_handler(void *arg, esp_event_base_t event_base,
                          int32_t event_id, void *event_data)
{
    if (event_base == WIFI_EVENT && event_id == WIFI_EVENT_STA_START)
    {
        esp_wifi_connect();
    }
    else if (event_base == WIFI_EVENT && event_id == WIFI_EVENT_STA_DISCONNECTED)
    {
        if (s_retry_num < ESP_MAXIMUM_RETRY)
        {
            esp_wifi_connect();
            s_retry_num++;
        }
        else
        {
            xEventGroupSetBits(s_wifi_event_group, WIFI_FAIL_BIT);
        }
        wifi_connect_status = 0;
    }
    else if (event_base == IP_EVENT && event_id == IP_EVENT_STA_GOT_IP)
    {
        ip_event_got_ip_t *event = (ip_event_got_ip_t *)event_data;
        s_retry_num = 0;
        xEventGroupSetBits(s_wifi_event_group, WIFI_CONNECTED_BIT);
        wifi_connect_status = 1;
    }
}

// Connect to WiFi
void connect_wifi(void)
{
    esp_err_t ret = nvs_flash_init();
    if (ret == ESP_ERR_NVS_NO_FREE_PAGES || ret == ESP_ERR_NVS_NEW_VERSION_FOUND)
    {
        ESP_ERROR_CHECK(nvs_flash_erase());
        ret = nvs_flash_init();
    }
    ESP_ERROR_CHECK(ret);

    s_wifi_event_group = xEventGroupCreate();

    ESP_ERROR_CHECK(esp_netif_init());

    ESP_ERROR_CHECK(esp_event_loop_create_default());
    esp_netif_create_default_wifi_sta();

    wifi_init_config_t cfg = WIFI_INIT_CONFIG_DEFAULT();
    ESP_ERROR_CHECK(esp_wifi_init(&cfg));

    esp_event_handler_instance_t instance_any_id;
    esp_event_handler_instance_t instance_got_ip;
    ESP_ERROR_CHECK(esp_event_handler_instance_register(WIFI_EVENT,
                                                        ESP_EVENT_ANY_ID,
                                                        &event_handler,
                                                        NULL,
                                                        &instance_any_id));
    ESP_ERROR_CHECK(esp_event_handler_instance_register(IP_EVENT,
                                                        IP_EVENT_STA_GOT_IP,
                                                        &event_handler,
                                                        NULL,
                                                        &instance_got_ip));

    wifi_config_t wifi_config = {
        .sta = {
            .ssid = ESP_WIFI_SSID,
            .password = ESP_WIFI_PASSWORD,
            .threshold.authmode = WIFI_AUTH_WPA2_PSK,
        },
    };
    ESP_ERROR_CHECK(esp_wifi_set_mode(WIFI_MODE_STA));
    ESP_ERROR_CHECK(esp_wifi_set_config(WIFI_IF_STA, &wifi_config));
    ESP_ERROR_CHECK(esp_wifi_start());

    EventBits_t bits = xEventGroupWaitBits(s_wifi_event_group,
                                           WIFI_CONNECTED_BIT | WIFI_FAIL_BIT,
                                           pdFALSE,
                                           pdFALSE,
                                           portMAX_DELAY);

    vEventGroupDelete(s_wifi_event_group);
}

// Envia a pagina HTML para o user
esp_err_t send_web_page(httpd_req_t *req)
{
    int response;
    char response_data[sizeof(html_page) + 50];
    memset(response_data, 0, sizeof(response_data));

    ESP_LOGI(TAG, "->TEMPERATURE TO WEB PAGE: %d°C:", temperature[0]);

    int len = snprintf(response_data, sizeof(response_data), html_page, temperature[0]);

    response = httpd_resp_send(req, response_data, len);

    return response;
}

// HTTP GET request handler for the main page
esp_err_t get_req_handler(httpd_req_t *req)
{
    return send_web_page(req);
}

httpd_uri_t uri_get = {
    .uri = "/",
    .method = HTTP_GET,
    .handler = get_req_handler,
    .user_ctx = NULL
};

// Prototypes for endpoint handler functions
static esp_err_t set_temperature_handler(httpd_req_t *req); // Mudar temperatura (nao a oficial)
static esp_err_t get_temperature_history_handler(httpd_req_t *req); // Ir buscar o historico das ultimas 30 temperaturas.
static esp_err_t sleep_req_handler(httpd_req_t *req);  // Meter o sensor a dormir
static esp_err_t wakeup_req_handler(httpd_req_t *req);  // Acordar o sensor de volta


// Mudar a temperatura (nao real) do sensor
httpd_uri_t uri_set_temperature = {
    .uri = "/set_temperature",
    .method = HTTP_GET,
    .handler = set_temperature_handler,
    .user_ctx = NULL
};

// See history endpoint
httpd_uri_t uri_get_temperature_history = {
    .uri = "/temperature_history",
    .method = HTTP_GET,
    .handler = get_temperature_history_handler,
    .user_ctx = NULL
};

// Sleep endpoint
httpd_uri_t uri_sleep = {
    .uri = "/sleep",
    .method = HTTP_GET,
    .handler = sleep_req_handler,
    .user_ctx = NULL
};

// Wakeup endpoint
httpd_uri_t uri_wakeup = {
    .uri = "/wakeup",
    .method = HTTP_GET,
    .handler = wakeup_req_handler,
    .user_ctx = NULL
};

// Handler for setting the temperature
static esp_err_t set_temperature_handler(httpd_req_t *req)
{
    char *buf;
    size_t buf_len;

    buf_len = httpd_req_get_url_query_len(req) + 1;
    if (buf_len > 1)
    {
        buf = malloc(buf_len);
        if (httpd_req_get_url_query_str(req, buf, buf_len) == ESP_OK)
        {
            char param[32];
            if (httpd_query_key_value(buf, "temp", param, sizeof(param)) == ESP_OK)
            {
                int temp = atoi(param);
                if (temp != 0 || strcmp(param, "0") == 0)
                {
                    temperature[0] = temp;
                    ESP_LOGI(TAG, "->TEMPERATURE TO WEB PAGE: %d°C:", temperature[0]);
                    update_leds();
                    httpd_resp_send(req, "Temperature set", HTTPD_RESP_USE_STRLEN);
                }
                else
                {
                    httpd_resp_send_err(req, HTTPD_400_BAD_REQUEST, "Invalid temperature value"); // para debug
                }
            }
            else
            {
                httpd_resp_send_err(req, HTTPD_400_BAD_REQUEST, "Temperature parameter not found"); // para debug
            }
        }
        else
        {
            httpd_resp_send_err(req, HTTPD_500_INTERNAL_SERVER_ERROR, "Server Error"); // para debug
        }
        free(buf);
    }
    else
    {
        httpd_resp_send_err(req, HTTPD_400_BAD_REQUEST, "No query parameters"); // paramentros invalidos
    }
    return ESP_OK;
}

// Handler for getting the temperature history
static esp_err_t get_temperature_history_handler(httpd_req_t *req)
{
    char response_data[1024];
    char temp_str[16];
    
    strcpy(response_data, "[");
    for (int i = HISTORICAL_SIZE - 1; i >= 0; i--)
    {
        snprintf(temp_str, sizeof(temp_str), "%d", historical_temperatures[(historical_index + i) % HISTORICAL_SIZE]);
        strcat(response_data, temp_str);
        if (i > 0)
        {
            strcat(response_data, ",");
        }
    }
    strcat(response_data, "]");

    httpd_resp_set_type(req, "application/json");
    httpd_resp_send(req, response_data, strlen(response_data));
    return ESP_OK;
}

// handler for putting the TC74 sensor in sleep mode
static esp_err_t sleep_req_handler(httpd_req_t *req)
{
    esp_err_t err = tc74_standy(sensorHandle);  // Usando a funcao do Tc74 Standy uint8_t buffer[2] = {0x01,0x80};
    if (err == ESP_OK)
    {
        sensor_sleeping = true; // Set the sleep flag
        turn_off_leds(); // Turn off LEDs when the sensor goes to sleep
        httpd_resp_send(req, "Sensor is in sleep mode", HTTPD_RESP_USE_STRLEN);
        ESP_LOGI(TAG, "Sensor is in sleep mode... /wakeup to wake him up");
    }
    else
    {
        httpd_resp_send_err(req, HTTPD_500_INTERNAL_SERVER_ERROR, "Failed to put sensor in sleep mode"); // para debug
    }
    return ESP_OK;
}

// handle the wakeup request
static esp_err_t wakeup_req_handler(httpd_req_t *req)
{
    esp_err_t err = tc74_wakeup(sensorHandle);  // Usar funcao do Tc74 wakeup {0x01,0x00};
    if (err == ESP_OK)
    {
        sensor_sleeping = false; // Clear the sleep flag
        update_leds(); // Turn on LEDs when the sensor wakes up
        httpd_resp_send(req, "Sensor is awake", HTTPD_RESP_USE_STRLEN);
        ESP_LOGI(TAG, "Sensor is awake!");
    }
    else
    {
        httpd_resp_send_err(req, HTTPD_500_INTERNAL_SERVER_ERROR, "Failed to wake up the sensor"); // para debug
    }
    return ESP_OK;
}

// Começar por dar setup ao HTTP server 
httpd_handle_t setup_server(void)
{
    httpd_config_t config = HTTPD_DEFAULT_CONFIG();
    config.stack_size = 8192; // Increase stack size (Nao funciona no edge por causa deste valor (fui testando))
    httpd_handle_t server = NULL;

    if (httpd_start(&server, &config) == ESP_OK)
    {
        httpd_register_uri_handler(server, &uri_get);  // Home page 
        httpd_register_uri_handler(server, &uri_set_temperature); // set temperature URI
        httpd_register_uri_handler(server, &uri_get_temperature_history); // history URI
        httpd_register_uri_handler(server, &uri_sleep); // sleep URI
        httpd_register_uri_handler(server, &uri_wakeup); //  wakeup URI
    }
    return server;
}


/*SD: */
static esp_err_t config_sd_card(){
    // Options for mounting the filesystem.
    // If format_if_mount_failed is set to true, SD card will be partitioned and
    // formatted in case when mounting fails.
    esp_err_t ret;
    esp_vfs_fat_sdmmc_mount_config_t mount_config = {
        #ifdef CONFIG_EXAMPLE_FORMAT_IF_MOUNT_FAILED
                .format_if_mount_failed = true,
        #else
                .format_if_mount_failed = false,
        #endif // EXAMPLE_FORMAT_IF_MOUNT_FAILED
                .max_files = 5,
                .allocation_unit_size = 16 * 1024
    };
 
    spi_bus_config_t bus_cfg = {
        .mosi_io_num = PIN_NUM_MOSI,
        .miso_io_num = PIN_NUM_MISO,
        .sclk_io_num = PIN_NUM_CLK,
        .quadwp_io_num = -1,
        .quadhd_io_num = -1,
        .max_transfer_sz = 4000,
    };
    ret = spi_bus_initialize(host.slot, &bus_cfg, SDSPI_DEFAULT_DMA);
    if (ret != ESP_OK) {
        ESP_LOGE(TAG, "Failed to initialize bus.");
    }

    sdspi_device_config_t slot_config = SDSPI_DEVICE_CONFIG_DEFAULT();
    slot_config.gpio_cs = PIN_NUM_CS;
    slot_config.host_id = host.slot;

    ret = esp_vfs_fat_sdspi_mount(mount_point, &host, &slot_config, &mount_config, &card);

    if (ret != ESP_OK) {
        if (ret == ESP_FAIL) {
            ESP_LOGE(TAG, "Failed to mount filesystem. "
                     "If you want the card to be formatted, set the CONFIG_EXAMPLE_FORMAT_IF_MOUNT_FAILED menuconfig option.");
        } else {
            ESP_LOGE(TAG, "Failed to initialize the card (%s). "
                     "Make sure SD card lines have pull-up resistors in place.", esp_err_to_name(ret));
        }
    }
    ESP_LOGI(TAG, "Filesystem mounted");
    
    // Card has been initialized, print its properties
    sdmmc_card_print_info(stdout, card);
    
    // Format FATFS
    ret = esp_vfs_fat_sdcard_format(mount_point, card);
    if (ret != ESP_OK) {
        ESP_LOGE(TAG, "Failed to format FATFS (%s)", esp_err_to_name(ret));
    } 
    //const char *file_temp = MOUNT_POINT"/log.txt";
   return ret; 
}
static esp_err_t s_example_write_file(const char *path, char *data)
{
    //ESP_LOGI(TAG, "Opening file %s", path);
    FILE *f = fopen(path, "a");
    if (f == NULL) {
        ESP_LOGE(TAG, "Failed to open file for writing");
        return ESP_FAIL;
    }
    fprintf(f, data);
    fclose(f);
   ESP_LOGI(TAG, "File written");

    return ESP_OK;
}

static esp_err_t s_example_read_file(const char *path)
{
    //ESP_LOGI(TAG, "Reading file %s", path);
    FILE *f = fopen(path, "r");
    if (f == NULL) {
        ESP_LOGE(TAG, "Failed to open file for reading");
        return ESP_FAIL;
    }
    char line[EXAMPLE_MAX_CHAR_SIZE];
    fgets(line, sizeof(line), f);
    fclose(f);

    // strip newline
    char *pos = strchr(line, '\n');
    if (pos) {
        *pos = '\0';
    }
    //ESP_LOGI(TAG, "Read from file: '%s'", line);

    return ESP_OK;
}

static void write_in_sd(){
    esp_err_t ret;
    char temperStr[10];
    itoa(temperature[0],temperStr,10);
    memset(data, 0, EXAMPLE_MAX_CHAR_SIZE);
    snprintf(data, EXAMPLE_MAX_CHAR_SIZE, "Temp: %s Date: %s\n", temperStr, strftime_buf);
    ret = s_example_write_file(file_temp, data);
    if (ret != ESP_OK) {
        ESP_LOGI(TAG, "Error writting file");
    }
    //Open file for reading
    ret = s_example_read_file(file_temp);
    if (ret != ESP_OK) {
        ESP_LOGI(TAG, "Error reading file");
    }
}
static void current_data(){
     // Obter hora atual
    time_t now;
    //char strftime_buf[64];
    struct tm timeinfo;

    time(&now);
    // Set timezone to west Europa Standard Time
    setenv("TZ", "WEST", 1);
    tzset();

    localtime_r(&now, &timeinfo);
    strftime(strftime_buf, sizeof(strftime_buf), "%c", &timeinfo);
}

static void turn_on_the_light(){
     if(temperature[0] < NORMAL){
        gpio_set_level(GPIO_NUM_2,1);
        gpio_set_level(GPIO_NUM_3,0);
        gpio_set_level(GPIO_NUM_5,0);

    }else if(temperature[0] >= NORMAL && temperature[0] < HOT){
        gpio_set_level(GPIO_NUM_2,0);
        gpio_set_level(GPIO_NUM_3,1);
        gpio_set_level(GPIO_NUM_5,0); 
    }else {
        gpio_set_level(GPIO_NUM_2,0);
        gpio_set_level(GPIO_NUM_3,0);
        gpio_set_level(GPIO_NUM_5,1);
    }
}
static void configure_TC74 (){
    uint8_t sensorAddr = 0x49;
    int sdaPin = SDA;
    int sclPin = SCL;
    uint32_t clkSpeedHz = 50000;
    tc74_init(&busHandle, &sensorHandle, sensorAddr, sdaPin, sclPin, clkSpeedHz);
    tc74_wakeup_and_read_temp(sensorHandle, temperature);
    past_temperature[0] = temperature[0]; 
}

void app_main(void)
{
    // Configure the I/O ports
    configure_gpio();
   
    /*SD configure: */
    esp_err_t ret;
    ret = config_sd_card();
   
    // Configure the sensor
    configure_TC74 ();

    connect_wifi();  // funcao para se conectar ao wifi ja com os dados fornecidos logo ao inicio
    if (wifi_connect_status)
    {
        setup_server();
    }

    // NUNO
    while (1)
    {
        if (!sensor_sleeping)
        {
            tc74_read_temp_after_temp(sensorHandle, temperature);
            current_data ();

            if(temperature[0] != past_temperature[0]){
                 write_in_sd();
            past_temperature[0] = temperature[0];
        }
            printf("Temperature: %d\r", temperature[0]);
            historical_temperatures[historical_index] = temperature[0];
            historical_index = (historical_index + 1) % HISTORICAL_SIZE;
            update_leds(); // Update LEDs here to function with the button to increase temperature
        }
        vTaskDelay(1000 / portTICK_PERIOD_MS);  // Tirar o portTick_PERIDO_MS
    }

    // All done, unmount partition and disable SPI peripheral
    esp_vfs_fat_sdcard_unmount(mount_point, card);
    ESP_LOGI(TAG, "Card unmounted");

    //deinitialize the bus after all devices are removed
    spi_bus_free(host.slot);
    //start_timers();
}
