#!/bin/bash

javac -cp ./src --module-path "javafx-sdk/lib" --add-modules=javafx.controls,javafx.fxml src/main/*.java
java -cp ./src --module-path "javafx-sdk/lib" --add-modules=javafx.controls,javafx.fxml main.Main
