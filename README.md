# Lake-Finder
The aim of this program is to make the desired changes in the matrix, where information on
which points are at what height, and then to calculate when money is poured as a liquid substance
in the final state of the terrain how much money will be accumulated at which points and the total
amount of money. I did this process under 3 classes, and I will summarize what these classes do
below:          
* **Main Class:** In this class, firstly, I created a map by extracting the data about the terrain from the
text file given to us, then I made the necessary modifications on this map with the valid inputs
entered to me via the command line. Then, I first thought of the final form of the map as a
rectangular prism that does not leak in any way and divided it into layers with as many units as the
highest length in the map. In this way, I found that if there was no infiltration in that layer, at which
point the wall was and at which point money would be formed. Then, on these layers, separately for
each layer, I performed a leakage test with my method written with the BFS searching algorithm
logic over the edges, while doing this, I looked to the right, left, bottom, top, and diagonals of each
coordinate. Then, after I reached the data on which points were leaking, I created my terrain by
placing these layers on top of each other. Here too, if that point does not leak and collects money, I
increased the value of that coordinate by one for each layer. And then I came up with a list with data
on which points I can collect money, how much money can accumulate on these points, and which
points are walls. After that, I found the coordinates of the lakes with a method written with the BFS
searching algorithm logic to find out if the coordinates that accumulated money form a lake, if so, in
which coordinates and how many volumes of money they have accumulated while doing this, I
looked to the right, left, bottom, top, and diagonals of each coordinate. Then I created a separate
"lake" object for each lake and kept them in a separate list. Then, I named these listed lake objects
alphabetically and printed the names of each of them on the terrain according to their coordinate
information. Finally, I printed the final version of the terrain and under that, I found the total money
volume of these lakes and printed it.      
* **Terrain Class:** In this class, I kept my data such as a row, column, list state of the map, number of
modifications, and final list state of my map. In addition to these, I have done operations such as
printing my map, modifying the map with valid inputs given on the command line to my map, and
creating the base part of my map with the methods belonging to this class.    
* **Lake Class:** In this class, I keep the coordinates of each lake object, the amount of money it holds in
those coordinates, the total amount of money, and its name. In addition, I calculate the total
amount of the money in the coordinates of my lake object with my only method belonging to this
class.

Example inputs and outputs are provided in the repository    

