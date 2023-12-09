# [Advent of Code 2023](https://adventofcode.com/2023)
![Kotlin](https://img.shields.io/badge/kotlin-%237F52FF.svg?style=for-the-badge&logo=kotlin&logoColor=white)

## [Day 1](https://adventofcode.com/2023/day/1)

Part 2 of the problem, adds the challenge that some numbers are written down as a words,
so I decided to substitute the word with the digit, and the first and last letters of the word,
breaking the word from getting read again, but not breaking the next word.

#### Possible optimization:
Use a regex to match only digits, or to read from both end of the string.

## [Day 2](https://adventofcode.com/2023/day/2)

Every line represents a "Game", followed by the sets of extractions.

#### Possible optimization:
Use a regex to group the different part of the line.

## [Day 3](https://adventofcode.com/2023/day/3)

Search for every number that has symbols ina 3x3 grid.

The solution is to create a map with the coordinates to digits and symbols:
after that:
1. search for every symbol
2. search around for any digits
3. search horizontally for other digits

Part 2 Required to search only for numbers around a specified symbol, and that had exactly 2 numbers around that symbol.

## [Day 4](https://adventofcode.com/2023/day/4)

Every line is a "card". Search for every winning number (on the left side) inside your numbers (on the right side).

Part 2 was tricky, because every X winning number added copies of card for the next X card, recursively.
My first (working) implementation followed this route, but the cost to calculate without proper caching was too high.
I later fixed my first idea, turning it in a linear solution, that multiply the number of winning copies.

## [Day 5](https://adventofcode.com/2023/day/5)

Is a translation problem, starting from a number you have to translate it through a translation table, to find the final number.

Part 2 was tricky, because instead a number you have to translate entire range of numbers;
so I decide to split and translate the bounds of the range, whenever a translation range was intersecting the given range. 

## [Day 6](https://adventofcode.com/2023/day/6)

You have to count all the possible configurations (how much time to spend to get more speed for the same time) to beat the old records of distance.

I started searching a mathematical solution;
but shortly after I noticed that it was faster to test the first half of the combinations, because it was mirrored in the center.  

## [Day 7](https://adventofcode.com/2023/day/7)

Identify the type of hand and the order

To solve this I created:
1. Class to represent the hand.
2. Comparable of the hand.
3. relative function to calculate type, and card value.

Part 2 require to handle the Jack card as Jolly cars, so extending the class to solve the problem

## [Day 8](https://adventofcode.com/2023/day/8)

The problem require to follow a path (described in 'L' and 'R') in a mesh of nodes (with 2 arches each) until reaching the end node.
Part 2 require to do this with multiple starting nodes at the same time, and needs to reach their end node at the same time.

A good solution was to use an iterator to follow the path, and a Map to easily access the node possible directions; 
but this is not good enough solution second part.
In fact my first implementation required a lot of time; 
initially I suspected it was allocation time because I was allocating a new collection every step.

after changing that I noticed that there is another taxing factor:
the **read-time of map** (taking nearly the 67% of the CPU time) is the next bottleneck.

If it's possible to lower the required time in accessing the map (or any substitution of it), that would increase the performance.

**Information:** to solve this problem in the day I decided to solve it in a different Language,
So I can't be sure the solution proposed is 100% correct, but it works with the test case

## [Day 9](https://adventofcode.com/2023/day/9)

The problem was to calculate the next value in the sequence.

the solution required:
1. sum in pair the values
2. check if the sums are all 0 (or check if they are all equal)
3. then calculate the next value of every line as the last element of the line plus the new element of the previous line

In alternative to point 3.:
accumulate the sums of every last item

The part 2 required to do the same, but with the previous element, so you just need to switch side, and operation