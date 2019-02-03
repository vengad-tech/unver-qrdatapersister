## Unver Intro

Uver allows you to convert any text files to QR code images. i.e it is ideally used to take backup of important data and store in paper as images.
Unver data-persister also encrypts the content using AES.

Currently the tool does not support binary data or compression. Support for those will be added in the future.

## Downloading
Download the tool and extract the ZIP to a folder: [Download link](https://github.com/vengadanathan/unver-qrdatapersister/raw/master/build/distributions/qrdatapersister-0.1.zip)

## Using the tool

### To encode text data to set of images (tool will ask password to encrypt content before encoding)

```
bin/qrdatapersister --encode -s <input_text_file> -d <output_folder_where_images_would_be_stored>
```

### To decode set of images into text

#### Writing to standard output instead of file

```
bin/qrdatapersister --decode -o -s <Image_folder_location>
```

### Writing output to file

```
bin/qrdatapersister --decode -s <Image_folder_location> -d <output_folder_where_images_would_be_stored>
```



### Other tool parameters

```
/bin/qrdatapersister -h
usage: [-h] --encode -s SOURCE [-d DESTINATION] [-o] [-r REDUNDANCY] [-p PIXEL]

required arguments:
  --encode, --decode          mode of operation

  -s SOURCE, --source SOURCE  when encoding it refers to source filename i.e
                              text file which we need to encode as OR images.
                              (currently only plain text files are supported)
                              When decoding source file refers to image to
                              decode. You can also point to directory that has
                              many images


optional arguments:
  -h, --help                  show this help message and exit

  -d DESTINATION,             when encoding, destination file path refers to
  --destination DESTINATION   folder when images would be stored When decoding
                              destination file path refers to folder where
                              text file would be written to

  -o, --stdout                enable verbose mode

  -r REDUNDANCY,              Redundancy factor
  --redundancy REDUNDANCY

  -p PIXEL, --pixel PIXEL     QR code pixel size
  ```