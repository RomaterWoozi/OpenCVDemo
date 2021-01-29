cmake_minimum_required(VERSION 3.6.0)
set(target detection_based_tracker)
project(${target} CXX)

set(ANDROID_OPENCV_COMPONENTS "opencv_java" CACHE STRING "")
message(STATUS "ANDROID_ABI=${ANDROID_ABI}")
find_package(OpenCV REQUIRED COMPONENTS ${ANDROID_OPENCV_COMPONENTS})

file(GLOB srcs *.cpp *.c)
file(GLOB hdrs *.hpp *.h)

include_directories("./include")
include_directories("${CMAKE_CURRENT_LIST_DIR}")
message(STATUS "CURRENT_DIR=${CMAKE_CURRENT_LIST_DIR}")
add_library(${target} SHARED ${srcs} ${hdrs})
target_link_libraries(${target} ${ANDROID_OPENCV_COMPONENTS})
# 声明文件地址
set(distribution_DIR ./libs)


#添加libavcodec库 SHARED类型 IMPORTED 引入
add_library(libavcodec
        SHARED
        IMPORTED)
# 设置 库属性IMPORTED_LOCATION
set_target_properties(libavcodec
        PROPERTIES IMPORTED_LOCATION
        ${distribution_DIR}/${ANDROID_ABI}/libavcodec.so)


#添加libavutil库 SHARED类型 IMPORTED 引入
add_library(libavutil
        SHARED
        IMPORTED)
# 设置 库属性IMPORTED_LOCATION
set_target_properties(libavutil
        PROPERTIES IMPORTED_LOCATION
        ${distribution_DIR}/${ANDROID_ABI}/libavutil.so)

#添加libswresample库 SHARED类型 IMPORTED 引入
add_library(libswresample
        SHARED
        IMPORTED)
# 设置 库属性IMPORTED_LOCATION
set_target_properties(libswresample
        PROPERTIES IMPORTED_LOCATION
        ${distribution_DIR}/${ANDROID_ABI}/libswresample.so)