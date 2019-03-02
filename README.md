# Lottery Mega Millions [2002.- 2019.] Statistics

The goal of this project was to get some hands-on experience with SAX and the entire XML parsing process. The main focus was,therefore, on the data extraction and some not to complicated statistics on the extracted data.

## Getting Started

If you are interested in taking a look at my solution at work, simply download the classes from this repository.
The relevant classes for deployment are :
"LotteryStats" (which holds the main class),"Log",
and it is recommended to check out the "StorageTests" class as well.
Additionally, you will need a data set to perform the operations on. The data set is located in the "LotteryWinning Numbers.xml" file. 
IMPORTANT NOTE : Make sure you reconfigure the location Path String in the "fileLoc" variable in the "LotteryStats" class.
(At the very top of the class)


### Prerequisites

A couple of packages will need to be downloaded in order to successfully run this project.

Package for core SAX APIs

```
org.xml.sax

This package provides the core SAX APIs. Some SAX1 APIs are deprecated to encourage integration of namespace-awareness into designs of new applications and into maintenance of existing infrastructure.

See http://www.saxproject.org for more information about SAX.
```

Package that provides classes allowing the processing of XML documents 

```
javax.xml.parsers

Provides classes allowing the processing of XML documents. Two types of plugable parsers are supported:
SAX (Simple API for XML)
DOM (Document Object Model)
```

API for unit testing.

```
JUnit 5.4.0 API
```


### Installing

```
Download the entire repository and copy the class content and imports.
```

```
Make sure the Path String to file location is properly configured.
```

## Running the tests

There is a single testing class for this project, and you can find it in this repository by the name "StorageTests".
The class was created to test if the stored data was properly placed and ordered. 

Test errorSlotEmpty()
```
- makes sure that no data is stored in the error slot
- if any data is found in the error slot, that indicates that 
  something went wrong in the calculateSlot() method being 
  called immediately upon the data object creation
- when data ends up in the error slot it indicates that a the  program received data that was out of bounds (2002. - 2019.)

```

Test noDistinctYears()
```
- makes sure that each linked list in the storage has only one 
  years data
- if any data with a wrong year is found in a particular slot, 
  the test will fail

```

## Authors

* **Jurica Kenda** - *Initial work* - [JuricaKenda](https://github.com/juricaKenda)

## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details

