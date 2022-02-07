# Build and install Delivery Optimization Client and SDK.
FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

DESCRIPTION = "Delivery Optimization Client"
HOMEPAGE = "https://github.com/microsoft/do-client.git"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=ee51f94efd0db5b258b5b1b8107fea02"

SRC_URI = "git://github.com/microsoft/do-client.git;protocol=https;tag=v0.8.2;nobranch=1;"

S = "${WORKDIR}/git"

DEPENDS = "boost msft-gsl"

inherit cmake features_check

EXTRA_OECMAKE += "-DCMAKE_BUILD_TYPE=Release"
EXTRA_OECMAKE += "-DDO_BUILD_TESTS=OFF"
EXTRA_OECMAKE += "-DDO_INCLUDE_SDK=ON"

BBCLASSEXTEND = "native nativesdk"
