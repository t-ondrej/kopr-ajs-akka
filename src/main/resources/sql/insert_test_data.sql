insert into
        Attendee("ID", "FIRSTNAME", "LASTNAME")
    values
         ('A1', 'First', 'Surname')
        ,('A2', 'Second', 'Surname')
        ,('A3', 'Third', 'Surname')
        ,('A4', 'Fourth', 'Surname');


insert into
        Subject("ID", "NAME")
    values
         ('S1', 'Algebra')
        ,('S2', 'English')
        ,('S3', 'Geography')
        ,('S4', 'Physics')
        ,('S5', 'Biology');


insert into
        Lecture("ID", "SUBJECT_ID")
    values
        ('L1', 'S1')
        ,('L2', 'S2')
        ,('L3', 'S3')
        ,('L4', 'S4')
        ,('L5', 'S5');

insert into
        AttendanceSheet("ID", "DAY", "LECTURE_ID")
    values
        ('T1', '2017-01-01', 'L1')
        ,('T2', '2017-01-02', 'L2')
        ,('T3', '2017-01-03', 'L3')
        ,('T4', '2017-01-04', 'L4')
        ,('T5', '2017-01-05', 'L5');

insert into
        AttendanceSheet_Attendee("ATTENDANCESHEET_ID", "ATTENDEE_ID")
    values
        ('T1', 'A1')
        ,('T2', 'A2')
        ,('T2', 'A3')
        ,('T4', 'A1');