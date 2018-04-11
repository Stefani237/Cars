drop   database if exists fastAndFurious;
create database fastAndFurious;
/*grant all privileges on *.* to 'scott'@'localhost' identified by 'tiger';*/
use fastAndFurious;

drop table if exists Gamble;
drop table if exists Person;
drop table if exists OpenRaces;
drop table if exists RacesHistory;
drop table if exists CarRacesHistory;
drop table if exists CarOpenRaces;

create table Person(personId char(9) not null, personName varchar(100),personPassword varchar(50) not null,cardNumber varchar(50) not null, userPermissions char(1), constraint pkPerson primary key (personId));
create table OpenRaces (raceId integer, startTime varchar(100), betsAmount double, raceStatus varchar(20), raceComments varchar(100), constraint pkRace primary key (raceId));
create table RacesHistory (raceId integer, startTime varchar(100), endTime varchar(100), results integer, betsAmount double, constraint pkRace primary key (startTime));
create table CarRacesHistory (carId integer, raceId integer, startTime varchar(100), carType varchar(100), carColor varchar(20),  carSize integer, carShape varchar(20), constraint fkStartTime foreign key (startTime) references RacesHistory(startTime));
create table CarOpenRaces (carId integer, raceId integer,carType varchar(100), carColor varchar(20),  carSize integer, carShape varchar(20), constraint fkRaceId foreign key (raceId) references OpenRaces(raceId));
create table Gamble (personId char(9) not null, raceId integer not null, carId integer, sumOfMoney double, profit double, constraint fkGamblePersonId foreign key (personId) references Person(personId),constraint fkOpenRaceId foreign key (raceId) references OpenRaces(raceId));
create table GamblesHistory (raceStartTime varchar(100),personId char(9) not null, raceId integer not null, carId integer, sumOfMoney double, profit double,totalSumOnWinningCar double,constraint fkPersonId foreign key (personId) references Person(personId),constraint fkRaceStartTime foreign key (raceStartTime) references RacesHistory(startTime));

commit;




