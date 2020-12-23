# Blind Pathfinder (Gold Miner)
This project is an intelligent system demonstrating the application of search strategies in the context of mostly-uninformed pathfinding in an environment with obstacles. In particular, the agent is a <b>gold miner</b> tasked to reach the square tile containing the pot of gold &mdash; the location of which is not disclosed to the agent &mdash; while avoiding obstacles (pits) and taking advantage of hints from beacons. 

It combines a modified depth-first search strategy with backtracking, memorization of obtained details regarding the environment, and miscellaneous decision-making rules to minimize the number of actions.

## Task
<b>Gold Miner</b> is the major course output for an introduction to intelligent systems class. Following the schema presented in the third edition of <i>Artificial Intelligence: A Modern Approach</i> by Russell and Norvig (2010), the PEAS description of the task is as follows:

- <b>Performance Measure:</b> The rationality of the agent is measured by the average number of actions (moves, rotates, scans, and backtracks) taken to reach the square tile containing the pot of gold.
- <b>Environment:</b> The environment is an <i>n</i> &times; <i>n</i> board, with the agent initially located on the upper left corner. Some tiles are <i>pits</i>, whihch will automatically trigger a game-over if the miner lands on one. There are also <i>beacons</i> that return the distance to the gold square tile provided that the gold square tile is located in any of the four cardinal directions of the beacon and there is no pit in between them. There is exactly one tile containing the <i>pot of gold</i>.
- <b>Actuators:</b> The agent has "actuators" for moving one tile forward and for rotating clockwise. 
- <b>Sensor:</b> The agent has a "sensor" that it can use to determine the designation of the tile in front if it; however, using this sensor (referred to as "scanning") adds to the total number of actions taken by the agent.

The project consists of three folders:
- <code>api</code> - <code>Javadoc</code> documentation of this project
- <code>bin</code> - <code>.class</code> files
- <code>src</code> - <code>.java</code> files (source codes)

Besides the <code>Gold Miner.jar</code> file, it also includes the following documents:
- <code>Running Program Interface.pdf</code> - Screenshots of the interface of the system
- <code>Technical Report.pdf</code> - Formal discussion of the behavior of the agent

## Using the Program
In the front-end interface, coordinates are one-based, that is, the upper left corner is designated as (1, 1), the tile to its right is (1, 2), and the tile below it is (2, 1). Use a single space to separate the row- and column-coordinates, and a newline to separate pairs. For instance, if the coordinates of the pits are (2, 3), (3, 4), and (4,5), then the input should be:
```
2 3
3 4
4 5
```

1. Open the <code>.jar</code> file.
2. Input the following information: <br/>
   a. Dimension (length) of the square board (between 8 and 64, inclusive) <br/>
   b. Coordinates of the tile containing the pot of gold <br/>
   c. Coordinates of the beacons <br/>
   d. Coordinates of the pits
   
   Note that the fields for (c) and (d) can be left blank should the user decide that beacons and/or pits are absent from the environment.
   
   <img src="https://github.com/memgonzales/blind-pathfinder/blob/main/system_screenshots/GoldMiner_1.JPG?raw=True" alt="Configuration" width = 300> 

3. Select the level of intelligence of the agent. <br/>

   <img src="https://github.com/memgonzales/blind-pathfinder/blob/main/system_screenshots/GoldMiner_2.JPG?raw=True" alt="AI Intelligence" width = 300> 

4. Select the speed at which the actions of the agent are displayed. <br/>

   <img src="https://github.com/memgonzales/blind-pathfinder/blob/main/system_screenshots/GoldMiner_3.JPG?raw=True" alt="Display Speed" width = 300>

5. Once the board is displayed, click Proceed to begin the pathfinding.

   <img src="https://github.com/memgonzales/blind-pathfinder/blob/main/system_screenshots/GoldMiner_4.JPG?raw=True" alt="Board" width = 750> 

## Built Using
This project was built using <b>Java</b>, with the <code>.class</code> files generated via <b>Java SE Development Kit 14</b>. The graphical user interface was created using <b>Swing</b>, a platform-independent toolkit that is part of the Java Foundation Classes. 

## Authors
- <b>Mark Edward M. Gonzales</b> <br/>
  mark_gonzales@dlsu.edu.ph <br/>
  gonzales.markedward@gmail.com <br/>
  
- <b>Hylene Jules G. Lee</b> <br/>
  hylene_jules_lee@dlsu.edu.ph <br/>
  lee.hylene@gmail.com
  
 Assets (images) are properties of their respective owners. Attribution is found in the file <code>src/gui/assets/asset-credits.txt</code>.
