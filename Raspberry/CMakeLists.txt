cmake_minimum_required(VERSION 3.13)
project (IoT)

set(THREADS_PREFER_PTHREAD_FLAG ON)

find_package(Threads REQUIRED) 

set(CMAKE_CXX_STANDARD 17)
set(CMAKE_CXX_STANDARD_REQUIRED ON)
set(CMAKE_CXX_EXTENSIONS OFF)

set (Match_VERSION_MAJOR 0)
set (Match_VERSION_MINOR 1)

MACRO(HEADER_DIRECTORIES return_list)
    FILE(GLOB_RECURSE new_list *.hpp *.h)
    SET(dir_list "")
    FOREACH(file_path ${new_list})
        GET_FILENAME_COMPONENT(dir_path ${file_path} PATH)
        SET(dir_list ${dir_list} ${dir_path})
    ENDFOREACH()
    LIST(REMOVE_DUPLICATES dir_list)
    SET(${return_list} ${dir_list})
ENDMACRO()

HEADER_DIRECTORIES(Headers)

file(GLOB_RECURSE Codes src/*.cpp src/*.h src/*.hpp)

add_executable(IoT ${Codes})

target_include_directories(IoT PRIVATE ${Headers})
target_link_libraries (IoT SimpleAmqpClient -lwiringPi Threads::Threads)


