#TODO adapt?
LICENSE = "CLOSED"

#TODO adapt to non dev branch
#SRC_URI = "git://dev.azure.com/conplementag/ICS_DeviceManagement/_git/bb-cplusplus-azure;protocol=https;branch=feature/6153_ubuntu_device_enrollment;user=${ICS_DM_GIT_CREDENTIALS}"
SRC_URI = "git://dev.azure.com/conplementag/ICS_DeviceManagement/_git/bb-cplusplus-azure;protocol=https;branch=marcel/feature/aduagent-tpm;user=${ICS_DM_GIT_CREDENTIALS}"
SRCREV = "${AUTOREV}"

DEPENDS = "azure-iot-sdk-c jq-native"
RDEPENDS_${PN} = "ca-certificates"

S = "${WORKDIR}/git/service-enrollment"

inherit cmake
EXTRA_OECMAKE += "-DINSTALL:DIR=bin"
EXTRA_OECMAKE += "-DBB_GITVERSION_INCLUDE_DIR=${BB_GIT_VERSION_INCLUDE_DIR}"
EXTRA_OECMAKE += "-DINSTALL_DIR=${bindir}"

inherit systemd
do_install_append() {
    install -d ${D}${sysconfdir}/ics_dm
    jq -n --arg dpsConnectionString "${ENROLLMENT_DPS_CONNECTION_STRING}" \
          --argjson edgeDevice "${IS_EDGE_DEVICE}" \
          --arg tag1 machine --arg tag1Value "${MACHINE}" \
          --arg tag2 tagName --arg tag2Value tagValue \
        '{ "dps_connectionString":"\($dpsConnectionString)",
           "edgeDevice": $edgeDevice,
           "tags" :
           { "\($tag1)" : "\($tag1Value)",
             "\($tag2)" : "\($tag2Value)"
        }}'  > ${D}${sysconfdir}/ics_dm/enrollment_static.conf
    install -d ${D}${systemd_system_unitdir}
    install -m 0644 ${S}/target/systemd/enrollment.service  ${D}${systemd_system_unitdir}
}
SYSTEMD_SERVICE_${PN} += "enrollment.service"
FILES_${PN} += "${systemd_system_unitdir}/enrollment.service"
REQUIRED_DISTRO_FEATURES = "systemd"
