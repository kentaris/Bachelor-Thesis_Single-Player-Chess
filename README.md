<img width="130" align="left" src="https://webmo.ch/wp-content/uploads/2022/03/csm_fakulogo_372ebdb784-2.png" alt="Philosophisch-Naturwissenschaftliche Fakultät (Faculty of Science) (Logo)">

Artificial Intelligence Research Group [![Artificial Intelligence Research Group (Website)](https://webmo.ch/wp-content/uploads/2022/03/External_Link_Logo.png)][AI Group]<br>
Department of Mathematics and Computer Science [![Department of Mathematics and Computer Science (Website)](https://webmo.ch/wp-content/uploads/2022/03/External_Link_Logo.png)][dmi]<br>
Philosophisch-Naturwissenschaftliche Fakultät (Faculty of Science) [![Faculty of Science (Website)](https://webmo.ch/wp-content/uploads/2022/03/External_Link_Logo.png)][philnat]<br>
University of Basel [![University of Basel (Webite)](https://webmo.ch/wp-content/uploads/2022/03/External_Link_Logo.png)][unibas]<br><br><br>

<img width="180" align="right" src="https://webmo.ch/wp-content/uploads/2022/03/logo-4-1.png" alt="Logo">

# Bachelor Thesis: Single Player Chess
<!-- TABLE OF CONTENTS -->
<details>
  <summary>Table of Contents</summary>
  <ol>
    <li><a href="#abstract">Abstract</a></li>
    <li><a href="#journal">Journal</a></li>
    <li><a href="#roadmap">Roadmap</a></li>
    <li><a href="#authors">Authors</a></li>
    <li><a href="#acknowledgments">Acknowledgments</a></li>
  </ol>
</details>

<!-- Abstract -->
## Abstract
<img width="300" align="right" src="https://webmo.ch/wp-content/uploads/2022/03/Screenshot-from-2022-03-04-15-52-21.png" alt="Terminal CHess UI">
The goal of this thesis is to model single-player Chess as a planning domain using the planning domain definition language (PDDL). We want to look into the problem of how to reach check mate position from an initial position. Both the initial and the goal position are given. As a first approach, we will encode this problem using PDDL. After this is done, we will also study how classical planners perform in this new domain implement a state-space search algorithm for this problem. 
<br clear="right"/>

<!-- Journal -->
## Journal
I am keeping a [Journal](Journal.md) of what I am doing so it is easier for me to time manage and also so it easier to write the final thesis. Feel free to have a look at it.

<!-- ROADMAP -->
## Roadmap
The roadblocks below build on on another so to see the relevant files for this project it is sufficient to check the folder linked in the last mentioned roadblock.

<!-- ROADBLOCKS -->
#### Roadblocks: 
1. [x] Getting familiar [![familiar](https://webmo.ch/wp-content/uploads/2022/03/External_Link_Logo.png)][1_First_attempt]
2. [x] Implement **Knight's Tour** [![.](https://webmo.ch/wp-content/uploads/2022/03/External_Link_Logo.png)][2_Knights Tour]
3. [x] Implement **Two Knight's Walk** [![.](https://webmo.ch/wp-content/uploads/2022/03/External_Link_Logo.png)][3_Two Knights Tour]
4. [x] Implement **white pieces only** and see if they can reach a given chess position. This is still a limited approach in in which special rules like castling are not implemented yet [![.](https://webmo.ch/wp-content/uploads/2022/03/External_Link_Logo.png)][4_White_Pieces_limitated]
5. [x] Implement **black pieces** as well [![.](https://webmo.ch/wp-content/uploads/2022/03/External_Link_Logo.png)][5_white&black_limitated]
6. [ ] Rewrite the problem using **derived predicates** so I can make use of recursion and axioms to implment all rules below.[![.](https://webmo.ch/wp-content/uploads/2022/03/External_Link_Logo.png)][6_all_rules_implemented]
   1. [x] normal moves
   2. [ ] capture moves
      1. [x] en passant
      2. [ ] pawn promotion
      3. [x] all other captures
   3. [x] check inhibited moves
      1. [x] absolute pin
      2. [x] in check
   4. [ ] Game termination (only checkmate) --> given if opponent's king is under attack and can't escape
   5. [x] other rules
      1. [x] double pawn move 
      2. [x] blocked movement (ex: bishop can't move trough a pawn)
      3. [x] castling
      4. [x] taking turns

See the [open issues](https://github.com/kentaris/Bachelor-Thesis_Single-Player-Chess/issues) for a full list of known issues.
Also there is a [discussions form](https://github.com/kentaris/Bachelor-Thesis_Single-Player-Chess/discussions/) for an alternative way of comminication.

<!-- Requirements -->
## Dependencies

* Numpy (via: `sudo apt-get install python3-numpy`)

<!-- Authors -->
## Authors

* Author: Ken Rotaris<br>
* Supervisor: [Augusto B. Corrêa](https://ai.dmi.unibas.ch/people/correa/)
* Professor: [Prof. Dr. Malte Helmert](https://ai.dmi.unibas.ch/people/helmert)

<!-- Acknowledgments -->
## Acknowledgments

* An Introduction to the Planning Domain Deﬁnition Language by Patrik Haslum and co. ([Book](https://www.morganclaypool.com/doi/abs/10.2200/S00900ED2V01Y201902AIM042))
* planing.domains ([Online PDDL Editor Tool](http://planning.domains/))
* Chess Logo inspired by [Marko Ivanovic](https://dribbble.com/shots/14950766/attachments/6667952?mode=media)

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
