# FileLocker
A console application written in Java that can "lock" and "unlock" files

Uses a `AES/ECB/PKCS5Padding` transformation algorithm to encrypt/decrypt the bytes of a given file

# Download
Download a sample executable jar or a zipped _.exe_ file in the [releases](https://github.com/ReiSchneider/FileLocker/releases) page

For the _.jar_ version, execute via (Replace <file name> with the file name of the downloaded jar)

```java -jar <file name>.jar```

_Make sure to run with at least a JDK version 16_

# Usage
In the console screen, select any of the following options by typing the associated number in the input line
* **1** to Lock Files
* **2** to Unlock previously locked files
* **0** to exit the application

For options 1 and 2, Enter the target file/folder path to be locked/unlocked

Type in your locking/unlocking password

Select y/n on whether you want to process sub-folders of the target path (if folder)

# Modification / Reimplementation 
Build, compile, and run with at least JDK version 16.

To redesign the console menu, modify `FileLockerConsole.java`

To reimplement the file locker service, modify `BasicFileLocker.java`

To provide or reimplement the encryption/decryption processes, modify `Encryptor.java`

## Disclaimer
The code provided is for education purpose only.

May corrupt files if used or implemented improperly. Do not use with illegal intent.

I will not be liable to You or anyone else for any decision made or action taken in reliance on the information given by the application or for any consequential, special or similar damages, even if advised of the possibility of such damages.
