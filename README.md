<h1>Nox Sudoku Solver</h1>
<h3>v1.0 2021-05-05</h3>

<p>
Application was developed as a way to test programming skills during programming course. As such it may not implement the most optimal solutions, in particular application does not implement Spring Framework.
</p>

There are two main parts of the app:

<ol>
<li> solving sudokus entered by user which is implemented through Sudoku and SudokuField classes. Sudoku class object consists of all possible values of every single field on sudoku board. Sudoku class uses all basic solving algorithms:
<ul>
<li>naked singles,</li>
<li>hidden singles,</li>
<li>naked pairs,</li>
<li>hidden pairs,</li>
<li>naked triples,</li>
<li>hidden triples,</li>
<li>naked quadruples,</li>
<li>hidden quadruples,</li>
<li>X-Wing,</li>
<li>XY-Wing,</li>
<li>Swordfish.</li>
</ul>
   More advanced algorithms may be added soon(tm). Sudoku class attempts to run known algorithms until one of the following conditions are met:
   <p>a) sudoku is solved</p>
   <p>b) sudoku is deemed invalid (there's a field which cannot contain any number without breaking rules)</p>
   <p>c) no algorithm is able to change state of sudoku</p>
   <br>In case of c) user may opt for backtracking solution. Due to performance issues option to backtrack is locked if there are more than 20 empty fields.
   Front part of having sudoku solve consists of fields to be filled by user. Entered values are being validated live, values other than 1-9 are discarded and any conflicts are ligthed up. Presence of value conflict locks possibility to send sudoku for solution search as it would return error. After sending partially filled sudoku Solve servlet class validates data returning error info if data is invalid and then attempts to solve sudoku. Solved or partially solved sudoku is then returned to view file with information about solution attempt result.
</li><br><li>
playing sudokus generated from database
   User chooses difficulty and then Game servlet class gets random seed of given difficulty from database. For replayability purposes seed is being altered through random shuffle of number, random turn and random mirroring.
   Saving and loading are implemented through cookies (locked if user did not agree to store cookies). Cookies store sudoku id, randomizer used, current state of the fields and timer value. Loaded game is of course validated in Game servlet.
   In both cases of new and loaded game solution seed is also generated and sent to view file, where it can be used in hint system.
   Fields generated at game start are read-only, in other fields user can enter any 1-9 digit as well as edit custom table of possible values in that particular field. JS functions provide live validation of entered values. As any game is played timer is measuring solving time. User may ask for hint which shows correct value of random empty or containing wrong number, hint is then locked for a set period of time. User has also possibility to reset game to remove any non-read-only values.
</li>
</ol>

<p>More options may be added in the future</p>

<h4>Copyright 2021 Zbigniew Toczko</h4>
<h4>e-mail: &#122;&#098;&#105;&#103;&#110;&#105;&#101;&#119;&#046;&#116;&#111;&#099;&#122;&#107;&#111;&#064;&#103;&#109;&#097;&#105;&#108;&#046;&#099;&#111;&#109;</h4>