# Electronic Competition Management System

## Program Usage

There are two scenarios for using the program:

* Processing applications and creating starting protocols
* Generating competition results based on data from checkpoints. The latter can only be used after
  creating the corresponding start protocol.

#### What will happen

* When the start scenario is launched, start protocols will be created for the groups and will be
  located in the `startProtocols` folder.

* The final scenario creates a `results.csv` file, which will contain the overall results for all
  age
  groups. A `teamResults.csv` file with team results will also be created.

The program can be launched using either command-line arguments or an advanced graphical interface.

### Via command-line arguments:

Launch the program from the **algorithm.kt** file.

To launch the start scenario, the command-line arguments must be as follows (and in this order):

| Argument 1 |       Argument 2       |         Argument 3          |                   Argument 4                    |
|:----------:|:----------------------:|:---------------------------:|:-----------------------------------------------:|
|  `start`   |      `event.csv`       |       `applications`        |                    `folder`                     |
|            | path to the event file | directory with applications | folder name for storing final results protocols |

For the final scenario:

| Argument 1 |                     Argument 2                     |  Argument 3   |         Argument 4          |                                 Аргумент 5<br/>                                  |
|:----------:|:--------------------------------------------------:|:-------------:|:---------------------------:|:--------------------------------------------------------------------------------:|
|  `finish`  |                   `classes.csv`                    | `courses.csv` |        `splits.csv`         |                                     `folder`                                     | |
|            | age group correspondence protocol for the distance | course routes | data on passing checkpoints | directory from the previous program run (you didn't forget to make it, did you?) |

### What happens in graphical interface:

#### When the program is launched, a startup window will appear.

The **"Documentation"** button will show you the documentation for the graphical interface on the
russian language.

The **"Results"** button will give an error until all the necessary data has been entered, as there
will
be no data to generate results from. To pass the starting data to the program and proceed to the
first stage of its work, you need to click the **"Create/Load"** button.

After clicking this button, the window enters a new state - **ImportState** in the program code.
![img_1.png](img_1.png)

The **"Back"** button returns the screen to its original state.

_In the text field_, enter the name of the folder where the start protocols will be saved.

Clicking the **"Competition file"** button opens a dialog box where you can select the CSV file with
the
competition data (you can see what it looks like below in the "About file formats" section).

Clicking the **"All registration files"** button opens a dialog box where you need to select the CSV
files - the participant registration files (you can see what they look like below in the "About file
formats" section).

![img_2.png](img_2.png)

After entering all the data, do not rush to exit this window - after
some time, the starting protocols will be generated and the button
"Starting protocols" will appear, and you can view them right in the program!
![img_3.png](img_3.png)

#### Second stage of the program. Uploading files for result generation

When you click the button "Competition data", a new window state opens:

![img_4.png](img_4.png)
When you click on the corresponding photo, upload the necessary files
through the dialog box:

**classes.csv** -> protocol for matching the age group with the distance
**courses.csv** -> routes of distances
**splits.csv** -> data on passing control points

Go back. When you click on the "Results" button, you can view the
results of the competition!

#### Lists

Also, by clicking on the Lists button, you can view the tables of distances, participants,
and data on passing control points by participants, implemented using
switchable tabs!

Distances: ![img_5.png](img_5.png) - you cannot change them in this version of the project.

Participants: ![img_6.png](img_6.png) - you cannot change them in this version of the project.

Data on passing control points by participants: ![img_7.png](img_7.png) -
you can edit them!
You can change information about the participant, by clicking on the **+**
add another passing of control points by the participant! The main thing is that other information
about them is added. In addition, you can delete
a row. Click the checkmark, and all changes will be saved!

### Shortly about file format

* Example of the competition file (CSV):

    ```csv
    Название,Дата
    Первенство пятой бани,01.01.2022
    ```

* Example of the application file (CSV):

    ```csv
    Выборгский СДЮШСОР №10,,,,,,,
    Группа,Фамилия,Имя,Г.р.,Разр.
    Иванов,Иван,2002,КМС,М21,,,
    Петров,Пётр,1978,I,М40,,,  
    Пупкин,Василий,2011,3ю,М10,,
    ```

* An example of the protocol of the course participant (CSV):

    ```csv
    Participant,243,
    1km,12:06:15
    2km,12:10:36
    Finish,12:14:51
    ```

* Example of a checkpoint protocol (CSV):

    ```csv
    Checkpoint,1km,
    241,12:04:17
    242,12:05:11
    243,12:06:15
    ```

* Example of a route category matching protocol (CSV):

    ```csv
    Название,Дистанция
    М10,МЖ9 10
    М12,М12
    М14,М14
    М16,М16 Ж60
    ```
* Example of route protocol (CSV) - the last check point is considered the finish point :

    ```csv
    Название,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25
    МЖ9 10,32,46,34,33,ЛюбоеСлово,,,,,,,,,,,,,,,,,,,,
    Ж14,47,46,45,34,33,32,48,52,51,50,49,53,,,,,,,,,,,,,
    Ж12,32,46,33,47,48,52,51,50,49,53,,,,,,,,,,,,,,,
    М12,32,46,34,35,45,33,47,48,52,49,53,,,,,,,,,,,,,,
    SUPER,21,!w22 w23,~2 24t 25t 26t 27t 28t,29,,,,,,,,,,,,,,,,,,,,,
    ```

Syntax for the distance task:

* Exclamation mark (!), tilde (~), comma (,), and space ( ) cannot be used in the names of control
  points.
* Blocks of the route are separated by commas and must be passed in the order written.
* A block consisting of a single control point name (,10, or ,AnyWord,) means that the participant
  must visit that control point.
* A block consisting of an exclamation mark (!) followed by the names of control points separated by
  spaces means that the participant must visit exactly one of the specified points of choice (,!w22
  w23, means that you can visit either w22 or w23).
* A block consisting of a tilde (~) followed by a natural number, and then the names of control
  points
  separated by spaces, means that the participant must visit exactly the specified number of
  different
  points in any order (,~2 24t 25t 26t 27t 28t, means that any two points in any order are suitable
  for the participant, for example (25t then 27t) or (27t then 25t), or (24t then 28t)).

We wish you successful use of the program. Read error messages - they contain information about what
went wrong while working with your data. You may need to change them for the program to work
correctly.
