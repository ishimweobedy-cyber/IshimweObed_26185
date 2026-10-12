@echo off
SET MVN_EXE=C:\Users\ishim\.m2\wrapper\dists\apache-maven-3.9.16\0daed3be3ebd1c706f0e69e8b07c6b73f5cc4ea3dfce72a8d0ec2e849ca2ddb0\bin\mvn.cmd
IF EXIST "%MVN_EXE%" (
    "%MVN_EXE%" %*
) ELSE (
    echo Maven not found at %MVN_EXE%
    echo Please install Maven and add it to PATH
    exit /b 1
)
