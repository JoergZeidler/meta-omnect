# Fluent Bit - Yocto / Bitbake
# ============================
# The following Bitbake package the latest Fluent Bit stable release.

SUMMARY = "Fast Log processor and Forwarder"
DESCRIPTION = "Fluent Bit is a data collector, processor and  \
forwarder for Linux. It supports several input sources and \
backends (destinations) for your data. \
"

HOMEPAGE = "http://fluentbit.io"
BUGTRACKER = "https://github.com/fluent/fluent-bit/issues"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=2ee41112a44fe7014dce33e26468ba93"
SECTION = "net"

SRCREV = "1d83649441e9e9fdbb5ba83bffca11ac4ac7b83c"
SRC_URI = "git://github.com/fluent/fluent-bit.git;protocol=https;nobranch=1 \
           file://0001-use-yaml-config-file.patch \
           file://fluent-bit.yaml \
           file://output-stdout.yaml \
           file://output-loki.yaml \
           "

S = "${WORKDIR}/git"
DEPENDS = "zlib bison-native flex-native openssl curl libyaml"
INSANE_SKIP_${PN}-dev += "dev-elf"

# Use CMake 'Unix Makefiles' generator
OECMAKE_GENERATOR ?= "Unix Makefiles"

# Fluent Bit build options
# ========================

# Host related setup
EXTRA_OECMAKE += "-DGNU_HOST=${HOST_SYS} "

# Disable LuaJIT and filter_lua support
EXTRA_OECMAKE += "-DFLB_LUAJIT=Off -DFLB_FILTER_LUA=Off "

# Disable Library and examples
EXTRA_OECMAKE += "-DFLB_SHARED_LIB=Off -DFLB_EXAMPLES=Off "

# Systemd support
DEPENDS += "systemd"
EXTRA_OECMAKE += "-DFLB_IN_SYSTEMD=On "

EXTRA_OECMAKE += "-DFLB_DEBUG=Off "
EXTRA_OECMAKE += "-DFLB_RELEASE=On "

inherit cmake systemd

TARGET_CC_ARCH += "${SELECTED_OPTIMIZATION}"


do_install:append () {
    install -m 0770 -D ${WORKDIR}/fluent-bit.yaml ${D}${sysconfdir}/fluent-bit/fluent-bit.yaml
    install -m 0770 -D ${WORKDIR}/output-stdout.yaml ${D}${sysconfdir}/fluent-bit/output-stdout.yaml
    install -m 0770 -D ${WORKDIR}/output-loki.yaml ${D}${sysconfdir}/fluent-bit/output-loki.yaml
}

SYSTEMD_SERVICE:${PN} = " fluent-bit.service"

FILES:${PN} += " \
  ${systemd_system_unitdir}/fluent-bit.service \
  ${sysconfdir}/fluent-bit/fluent-bit.yaml \
  ${sysconfdir}/fluent-bit/output-stdout.yaml \
  ${sysconfdir}/fluent-bit/output-loki.yaml \
  "