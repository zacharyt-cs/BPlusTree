# Introduction
This project is to design and implement the following two
components of a database management system, storage and indexing.

(1) For the storage component, the following settings are assumed:
- a fraction of main memory is allocated to be used as disk storage for
simplicity and the disk capacity could be 100 - 500 MB (depending on your
machine’s main memory configuration)
- the block size is 100 B

(2) For the indexing component, assume that a B+ tree is used.

## Index

- [About](#about)
- [Installation](#installation)
- [Development](#development)
  - [Development Environment](#development-environment)
  - [File Structure](#file-structure)
- [Gallery](#gallery)
- [Credit/Acknowledgment](#creditacknowledgment)

## About
Function 1: Store the data (which is about IMDb movives and described
in Part 4) on the disk and report the following statistics:
- the number of blocks
- the size of database

Function 2: Build a B+ tree on the attribute "averageRating" by inserting
the records sequentially and report the following statistics:
- the parameter n of the B+ tree
- the number of nodes of the B+ tree
- the height of the B+ tree, i.e., the number of levels of the B+ tree
- the root node and its child nodes (actual content)

Function 3: Retrieve the attribute “tconst” of those movies with the
“averageRating” equal to 8 and report the following statistics:
- the number and the content of index nodes the process accesses
- the number and the content of data blocks the process accesses
- the attribute “tconst” of the records that are returned

Function 4: Retrieve the attribute “tconst” of those movies with the
attribute “averageRating” from 7 to 9, both inclusively and report the
following statistics:
- the number and the content of index nodes the process accesses
- the number and the content of data blocks the process accesses
- the attribute “tconst” of the records that are returned

Function 5: Re-set the block size to be 500 B and re-do Function 1-4.

## Data

The data contains the IMDb rating and votes information for movies
- tconst (string) – alphanumeric unique identifier of the title
- averageRating – weighted average of all the individual user ratings
- numVotes – number of votes the title has received

## Installation
Launch Eclipse IDE and set project directory as workspace.

## Development
Possible feature:
- Delete a key and update the B+ tree accordingly

### Development Environment
Java JDK 8

### File Structure
Add a file structure here with the basic details about files, below is an example.

| No | File Name | Details 
|----|------------|-------|
| 1  | Main.java | Run this file |
| 2  | data.tsv | Full dataset |
| 3  | data(30k).tsv | Dataset with 30k rows of data |

In some cases, running main.java with the full dataset will result in heap overflow.

##  Gallery
| Menu     | Example     |
|------------|-------------|
| <img src="img/main.jpg" width="400"> | <img src="img/exp2.png" width="400"> |

## Credit/Acknowledgment
Collaborated with @gitteroy and @EvoYX
