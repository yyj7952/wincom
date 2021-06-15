################################################################################
# Automatically-generated file. Do not edit!
################################################################################

# Add inputs and outputs from these tool invocations to the build variables 
C_SRCS += \
../CACHE/cache_nxvod900_t.c \
../CACHE/cache_nxvod920_t.c 

OBJS += \
./CACHE/cache_nxvod900_t.o \
./CACHE/cache_nxvod920_t.o 

C_DEPS += \
./CACHE/cache_nxvod900_t.d \
./CACHE/cache_nxvod920_t.d 


# Each subdirectory must supply rules for building sources it contributes
CACHE/%.o: ../CACHE/%.c
	@echo 'Building file: $<'
	@echo 'Invoking: Cygwin C Compiler'
	gcc -O0 -g3 -Wall -c -fmessage-length=0 -MMD -MP -MF"$(@:%.o=%.d)" -MT"$(@:%.o=%.d)" -o "$@" "$<"
	@echo 'Finished building: $<'
	@echo ' '


