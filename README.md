# FileLocker
A console application written in Java 11 that can 'lock' and 'unlock' files

# How to run 
Make sure to compile and run with at least Java 11.

Import the project to your IDE and run the `main` method from the `FileLockerConsole` console

## How to implement
Implement the interface ```FileLocker``` and provide method definitions for the methods `encrypt(String path, String key)` and `decrypt(String path, String key)`

## How to use

Modify `FileLockerFactory` and provide own implementation for the `getFileLocker()` method

Get an instance of the `FileLocker` interface by calling the static method `getFileLocker()` of the `FileLockerFactory` class

Encrypt/decrypt a file or files in a directory given a valid path, and an optional encryption/decryption key, using the `encrypt()` and `decrypt()` methods

## Disclaimer
The code provided is for education purpose only.

May corrupt files if used or implemented improperly. Do not use with illegal intent.

I will not be liable to You or anyone else for any decision made or action taken in reliance on the information given by the application or for any consequential, special or similar damages, even if advised of the possibility of such damages.
