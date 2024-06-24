#include <string.h>
#include <sys/unistd.h>
#include <sys/stat.h>
#include "esp_vfs_fat.h"
#include "sdmmc_cmd.h"


esp_err_t s_example_write_file(const char *path, char *data);
esp_err_t s_example_read_file(const char *path);
esp_err_t config_init(int PIN_NUM_MOSI, int PIN_NUM_MISO, int PIN_NUM_CLK,int PIN_NUM_CS, char mount_point[], sdmmc_card_t *card, sdmmc_host_t host);
