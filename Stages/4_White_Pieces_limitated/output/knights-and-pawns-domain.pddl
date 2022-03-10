(define (domain knights-and-pawns)
    (:requirements :typing :negative-preconditions :equality)
    (:types
        figure - object
        location - object
        white - figure
        ;black - figure
        pawn_w knight_w - white
        ;knight_b - black

    )
    (:predicates
        (at ?figure - figure ?file ?rank - location)
        (diff_by_Zero ?file ?rank - location)
        (diff_by_One ?file ?rank - location)
        (diff_by_Two ?file ?rank - location)
        (plusOne_white ?file ?rank - location)
        (plusOne_black ?file ?rank - location)
        (pawn_double_white ?from_file ?to_file ?from_rank ?to_rank - location)
        (pawn_double_black ?from_file ?to_file ?from_rank ?to_rank - location)
    )

    (:action pawn_move_double_white
        :parameters (?pawn - pawn_w ?from_file ?to_file ?from_rank ?to_rank - location)
        :precondition (and (at ?pawn ?from_file ?from_rank)
                           (pawn_double_white ?from_file ?to_file ?from_rank ?to_rank)
                      )
        :effect (and (not (at ?pawn ?from_file ?from_rank))
                     (at ?pawn ?to_file ?to_rank)
                )
    )

    (:action pawn_move_single_white
        :parameters (?pawn - pawn_w ?from_file ?to_file ?from_rank ?to_rank - location)
        :precondition (and (at ?pawn ?from_file ?from_rank)
                           (diff_by_Zero ?from_file ?to_file) ; file +/- 0
                           (plusOne_white ?from_rank ?to_rank) ; rank +1
                      )
        :effect (and (not (at ?pawn ?from_file ?from_rank))
                     (at ?pawn ?to_file ?to_rank)
                )
    )

    (:action knight_move_2file_1rank
        :parameters (?knight1 ?knight2 - knight_w ?from_file ?to_file ?from_rank ?to_rank - location)
        :precondition (and (at ?knight1 ?from_file ?from_rank)
                           (diff_by_Two ?from_file ?to_file) ; file +/- 2
                           (diff_by_One ?from_rank ?to_rank) ; rank +/- 1
                           (not (at ?knight2 ?to_file ?to_rank))
                           (not (at ?knight1 ?to_file ?to_rank))
                           (not (= ?knight1 ?knight2))
                      )
        :effect (and (not (at ?knight1 ?from_file ?from_rank))
                     (at ?knight1 ?to_file ?to_rank)
                )
    )
    (:action knight_move_2rank_1file
        :parameters (?knight1 ?knight2 - knight_w ?from_file ?to_file ?from_rank ?to_rank - location)
        :precondition (and (at ?knight1 ?from_file ?from_rank)
                           (diff_by_Two ?from_rank ?to_rank) ; rank +/- 2
                           (diff_by_One ?from_file ?to_file) ; file +/- 1
                           (not (at ?knight2 ?to_file ?to_rank))
                           (not (at ?knight1 ?to_file ?to_rank))
                           (not (= ?knight1 ?knight2))
                           )
        :effect (and (not (at ?knight1 ?from_file ?from_rank))
                     (at ?knight1 ?to_file ?to_rank)
                )
    )
)