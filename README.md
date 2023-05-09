# Sports Competition Management System

## Domain description (Full documentation is provided [here](DOCS.md).)

Suppose you need to implement an organizational system for sports competitions in one of the cyclic
sports: running, cross-country skiing, swimming, cycling, orienteering, etc.

In the simplest case, competitions have a name, a date of the event, and imply the passage of some
distance by each athlete.

All athletes perform in different groups depending on gender and age. The list of groups is
determined by the regulations of the competition and is published in advance.

Each group has its own distance, and some groups may have the same distance.

Athletes compete for different teams, each team submits application lists, in which the surname,
name, year of birth, sports category, and desired group for athletes are indicated. The application
lists also include information about medical examination and accident insurance for each athlete.

Based on all the application lists, a start protocol is formed for each group. The protocol is
formed as a result of drawing. Each athlete receives an individual bib number and start time. The
start can be common (at the same time) or separate. In the simplest case, the drawing places all
athletes in the group in random order. However, more complex types of drawing can be used, for
example, when it is necessary to take into account the category, consider the run within the group,
and take into account the simultaneous start of different groups.

After the competition, a protocol of results is formed, as well as a protocol with intermediate
results of passing the distance. The distance may consist of several checkpoints, each of which
records the passing time. The result is recorded manually or with the help of one or several
electronic marking systems. Accordingly, the result is entered into the system either when manually
entered or when receiving data from electronic marking systems. Usually, this is either a list of
the form <number> - <time> for a given checkpoint or a list of the form <checkpoint> - <time> for a
given number (athlete).

In the start, finish, and other protocols, it is necessary to indicate the number, name, surname,
year of birth, sports category, and team for each participant. In the start protocol, the start time
is additionally indicated. The protocol of results indicates the final place, the result (the time
spent on passing the distance), the lag from the leader, and (optionally) the achieved sports
category. The achieved sports category is calculated using a certain formula, which depends on the
type of sport, group, and regulations of specific competitions. In addition to the protocol of
results, a protocol of results for teams is formed for each group. At the same time, according to a
certain formula, depending on the regulations of specific competitions, the result of each athlete in his/her group
gives a certain number of points, which together give the result of the team.

Example of the entry list (CSV):

```csv
Выборгский СДЮШСОР №10,,,,,,,
Иванов,Иван,2002,КМС,М21,,,
Петров,Пётр,1978,I,М40,,,  
Пупкин,Василий,2011,3ю,М10,,
```

Example of the start protocol for the group (CSV):

```csv
М10,,,,,,
241,Пупкин,Василий,2011,3ю,12:01:00,
242,Пирогов,Григорий,2011,3ю,12:02:00
243,Смирнов,Сергей,2012,,12:03:00
```

An example of the protocol of the course participant (CSV):

```csv
243,,
1km,12:06:15
2km,12:10:36
Finish,12:14:51
```

Example of a checkpoint protocol (CSV):

```csv
1km,,
241,12:04:17
242,12:05:11
243,12:06:15
```

Example of the result protocol (CSV):

```csv
М10,,,,,,,
1,242,Пирогов,Григорий,2011,3ю,00:12:51,
2,243,Смирнов,Сергей,2012,,00:12:57,
3,241,Пупкин,Василий,2011,3ю,00:13:15
```

