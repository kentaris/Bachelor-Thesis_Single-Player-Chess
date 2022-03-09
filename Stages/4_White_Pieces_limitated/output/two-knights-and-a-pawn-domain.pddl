(define (domain two-knights-and-a-pawn)
    (:requirements :typing :negative-preconditions :equality)
    (:types
        figure - object
        location - object
        white - figure
        ;black - figure
        rank file - location
        pawn_w knight_w - white
        ;knight_b - black

    )
    (:predicates
        (at ?knight - knight ?file - file ?rank - rank)
        (visited ?file -  file ?rank - rank)
        (diff_by_zero ?file ?rank - location)
        (diff_by_one ?file ?rank - location)
        (diff_by_two ?file ?rank - location)
    )
    (:action pawn_move_double_white
        :parameters (?pawn1 ?pawn2 ?pawn3 ?pawn4 ?pawn5 - pawn ?from_file ?to_file - file ?from_rank ?to_rank - rank)
        :precondition (and (at ?pawn1 ?from_file ?from_rank)
                           (diff_by_two ?from_rank ?to_rank)
                           (diff_by_zero ?from_file ?to_file)
                           (not (= ?pawn1 ?pawn2 ?pawn3 ?pawn4 ?pawn5))
                      )
        :effect (and )
    )
    

    (:action knight_move_2file_1rank
        :parameters (?knight1 ?knight2 - knight ?from_file ?to_file -  file ?from_rank ?to_rank - rank)
        :precondition (and (at ?knight1 ?from_file ?from_rank)
                           (diff_by_two ?from_file ?to_file) ; file +/- 2
                           (diff_by_one ?from_rank ?to_rank) ; rank +/- 1
                           (not (at ?knight2 ?to_file ?to_rank))
                           (not (at ?knight1 ?to_file ?to_rank))
                           (not (= ?knight1 ?knight2))
                      )
        :effect (and (not (at ?knight1 ?from_file ?from_rank))
                     (at ?knight1 ?to_file ?to_rank)
                     (visited ?to_file ?to_rank))
    )
    (:action knight_move_2rank_1file
        :parameters (?knight1 ?knight2 - knight ?from_file ?to_file -  file ?from_rank ?to_rank - rank)
        :precondition (and (at ?knight1 ?from_file ?from_rank)
                           (diff_by_two ?from_rank ?to_rank) ; rank +/- 2
                           (diff_by_one ?from_file ?to_file) ; file +/- 1
                           (not (at ?knight2 ?to_file ?to_rank))
                           (not (at ?knight1 ?to_file ?to_rank))
                           (not (= ?knight1 ?knight2))
                           )
        :effect (and (not (at ?knight1 ?from_file ?from_rank))
                     (at ?knight1 ?to_file ?to_rank)
                     (visited ?to_file ?to_rank))
    )
)