<img width="130" align="left" src="https://webmo.ch/wp-content/uploads/2022/03/csm_fakulogo_372ebdb784-2.png" alt="Philosophisch-Naturwissenschaftliche Fakultät (Faculty of Science) (Logo)">

Artificial Intelligence Research Group [![Artificial Intelligence Research Group (Website)](https://webmo.ch/wp-content/uploads/2022/03/External_Link_Logo.png)][AI Group]<br>
Department of Mathematics and Computer Science [![Department of Mathematics and Computer Science (Website)](https://webmo.ch/wp-content/uploads/2022/03/External_Link_Logo.png)][dmi]<br>
Philosophisch-Naturwissenschaftliche Fakultät (Faculty of Science) [![Faculty of Science (Website)](https://webmo.ch/wp-content/uploads/2022/03/External_Link_Logo.png)][philnat]<br>
University of Basel [![University of Basel (Webite)](https://webmo.ch/wp-content/uploads/2022/03/External_Link_Logo.png)][unibas]<br><br><br>

<img width="180" align="right" src="https://webmo.ch/wp-content/uploads/2022/03/logo-4-1.png" alt="Logo">

# Solving Single-Player Chess

This is the official GitHub repository of my Bachelor's thesis. The written thesis is located in this repository or can alternatively be viewed on [Overleaf](https://www.overleaf.com/read/hzkvjknryssm). The finish date was agreed to be the 07.06.2022.

<!-- TABLE OF CONTENTS -->
<details>
  <summary>Table of Contents</summary>
  <ol>
    <li><a href="#abstract">Abstract</a></li>
    <li><a href="#journal">Journal</a></li>
    <li><a href="#roadblocks">Roadblocks</a></li>
    <li><a href="#dependencies">Dependencies</a></li>
    <li><a href="#authors">Authors</a></li>
    <li><a href="#acknowledgments">Acknowledgments</a></li>
    <li><a href="#additional-implementations">Additional Implementations</a></li>
    
  </ol>
</details>

<!-- Abstract -->
## Abstract

<img width="300" align="right" src="https://webmo.ch/wp-content/uploads/2022/03/Screenshot-from-2022-03-04-15-52-21.png" alt="Terminal CHess UI">
This thesis will look at Single-Player Chess as a planning domain using two approaches: one where we look at how we can encode the Single-Player Chess problem as a domain-independent (general-purpose AI) approach and one where we encode the problem as a domain-specific solver. Lastly, we will compare the two approaches by doing some experiments and comparing the results of the two approaches.<br><br>
Both the domain-independent implementation and the domain-specific implementation differ from traditional chess engines because the task of the agent is not to find the best move for a given position and colour, but the agent's task is to check if a given chess problem has a solution or not. If the agent can find a solution, the given chess puzzle is valid.<br><br>
The results of both approaches were measured in experiments, and we found out that the domain-independent implementation is too slow and that the domain-specific implementation, on the other hand, can solve the given puzzles reliably, but it has a memory bottleneck rooted in the search method that was used.
<br clear="right"/>

## Usage
### PDDL solver
To ensure that the paths are set correctly, the Fast Downward root repository needs to be specified in the file "populate_PDDL.py" in lines 13 and 15. The problem must be given as a FEN code pair in lines 207 and 208 (in "populate_PDDL.py"). The size of the FEN code can be defined by the user in the "population_generator.py" file in line 5 and must match the given FEN code pair in length (Suppose the the FEN code is "B2/3/ppp" then board_size must be 3). If one wishes to activate the precomputed predicates or add precomputed predicates, this can be done in populate_PDDL.py in the load_file()-function.
### Domain specific solver
The user can execute the search by calling the main function located in the class "Search.java" with the following arguments: "--Greedy" to run the greedy best-first search, "--A*" to execute the A-star search, "--A* w" to execute weighted A-star search (where w must be a decimal number) and "--Breadth" to execute the basic breadth-first search. The problem can be given as a FEN code pair, or a intuitive chessboard like arranged array pair in the "Problem.java" class in the function initialize(). To switch between the two mentioned input options, the corresponding (marked) lines need to be toggled (uncommented and commented). The player whose turn is must be defined either way in the "whitesTurn" variable ("Problem.java" in line 30).

<!-- Journal -->
## Journal

A [Journal](Journal.md) was kept during the full process. Feel free to have a look at it.

<!-- ROADBLOCKS -->
#### Roadblocks: 

The following simplified roadblocks are here so the reader can understand how the project timeline was roughly organized. To get more insight about this, we encourage the reader to look at [the Journal](Journal.md).
1. [x] Getting familiar
2. [x] Implement **Knight's Tour** 
3. [x] Implement **Two Knight's Walk** 
4. [x] Implement **white pieces only** and see if they can reach a given chess position. This is still a limited approach in which special rules like castling are not implemented yet
5. [x] Implement **black pieces** as well
6. [x] Rewrite the problem using **derived predicates** so we can use recursion and axioms to implement all rules below.
   1. [x] normal moves
   2. [x] capture moves
      1. [x] en passant
      2. [x] pawn promotion
      3. [x] all other captures
   3. [x] check inhibited moves
      1. [x] absolute pin
      2. [x] in check
   4. [x] Game termination (only checkmate) --> given if the opponent's king is under attack and can't escape
   5. [x] other rules
      1. [x] double pawn move 
      2. [x] blocked movement (ex: bishop can't move through a pawn)
      3. [x] castling
      4. [x] taking turns
7. [x] Write a state-space algorithm that solves single-player chess
   1. [x] writing a Move generator using a 12x64bit array (one bitboard for every figure of every colour)
   2. [x] implement best-first-search and see if it works for simple cases.
   3. [x] implement Greedy-best-first-search and use priority queue
8. [x] Finishing the thesis document on [Overleaf](https://www.overleaf.com/read/hzkvjknryssm).

See the [open issues](https://github.com/kentaris/Bachelor-Thesis_Single-Player-Chess/issues) for a complete list of known issues.
Also, there is a [discussions form](https://github.com/kentaris/Bachelor-Thesis_Single-Player-Chess/discussions/) for an alternative way of communication.

<!-- Requirements -->
## Dependencies

* Fast downward: [Github](https://github.com/aibasel/downward), [Website](https://www.fast-downward.org/) 
* [Python-Chess](https://python-chess.readthedocs.io/en/latest/) Library

<!-- Authors -->
## Authors

* Author: Ken Rotaris<br>
* Supervisor: [Augusto Blaas Corrêa](https://ai.dmi.unibas.ch/people/correa/)
* Professor: [Prof. Dr. Malte Helmert](https://ai.dmi.unibas.ch/people/helmert)

## Additional Implementations
Some of the additional work that has been done in this thesis includes:
 - Eye appealing terminal chess user interface which is usable for any given board size(a snapshot can be seen at the head of this ReadMe)
 - Decoding of the PDDL plan files into a terminal plan which is printed out in standard chess notation ('O-O' for kingside castling, for example)
 Decoding the found action plan of the domain dependant solver into standard chess notation.
 - Validation of the PDDL action plan using the python chess library (every output plan receives a tag if it is a valid plan or not according to the engine, and it doesn't matter what size the board is because it is preprocessed to have dimensionality of 8x8 so the library can process it.)
 - The domain-dependent solver was implemented using Bitmaps.

<!-- Acknowledgments -->
## Acknowledgments
* On January 9th 2022, I first contacted [Prof. Dr. Malte Helmert](https://ai.dmi.unibas.ch/people/helmert) with the request to write my thesis with the Artificial Intelligence Research Group, and I’m grateful for not only the opportunity he gave me but also the generous topic suggestions, which included my wish to write a thesis in a chess-related topic.
* This thesis would not have been possible without my supervisor [Augusto Blaas Corrêa](https://ai.dmi.unibas.ch/people/correa/). He always provided invaluable feedback, pointers, and suggestions in our scheduled weekly meetings and with prompt feedback via email.
* The book "An Introduction to the Planning Domain Deﬁnition Language by Patrik Haslum and co." ([Book](https://www.morganclaypool.com/doi/abs/10.2200/S00900ED2V01Y201902AIM042)) helped me a lot to get started with PDDL.
* The planing.domains ([Online PDDL Editor Tool](http://planning.domains/)) tool helped me to get started using PDDL.
* The [Fast Downward](https://github.com/aibasel/downward) planner enabled me to use a state of the art PDDL planner, which made it possible for me to make use of more powerful aspects of the PDDL language such as axioms.
* The [Basilea-latex Template](https://github.com/ivangiangreco/basilea-latex) used to write the thesis was created by Ivan Giangreco.
* The Chess Logo in this README was inspired by [Marko Ivanovic](https://dribbble.com/shots/14950766/attachments/6667952?mode=media)'s Chess Logo.

[AI Group]: https://ai.dmi.unibas.ch/
[dmi]: https://dmi.unibas.ch/en/
[philnat]: https://philnat.unibas.ch/
[unibas]: https://www.unibas.ch/
[1_First_attempt]: https://github.com/kentaris/Bachelor-Thesis_Single-Player-Chess/tree/main/Bachelorarbeit/Stages/1_First_attempt
[2_Knights Tour]: https://github.com/kentaris/Bachelor-Thesis_Single-Player-Chess/tree/main/Bachelorarbeit/Stages/2_Knights_Tour
[3_Two Knights Tour]: https://github.com/kentaris/Bachelor-Thesis_Single-Player-Chess/tree/main/Bachelorarbeit/Stages/3_Two_Knights_Tour
[4_White_Pieces_limitated]: https://github.com/kentaris/Bachelor-Thesis_Single-Player-Chess/tree/main/Bachelorarbeit/Stages/4_White_Pieces_limitated
[5_white&black_limitated]: https://github.com/kentaris/Bachelor-Thesis_Single-Player-Chess/tree/main/Bachelorarbeit/Stages/5_white&black_limitated
[6_all_rules_implemented]: https://github.com/kentaris/Bachelor-Thesis_Single-Player-Chess/tree/main/Bachelorarbeit/Stages/6_all_rules_implemented