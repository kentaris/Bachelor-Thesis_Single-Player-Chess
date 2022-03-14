(define (domain knights-and-pawns)
    (:requirements :typing :negative-preconditions :equality :disjunctive-preconditions)
    (:types
        figure - object
        location - object

        pawn knight bishop rook queen king - figure
        white black - color

        pawn_w   pawn_b   - pawn  
        knight_w knight_b - knight
        bishop_w bishop_b - bishop
        rook_w   rook_b   - rook  
        queen_w  queen_b  - queen 
        king_w   king_b   - king  

        pawn_w knight_w bishop_w rook_w queen_w king_w - white
        pawn_b knight_b bishop_b rook_b queen_b king_b - black
    )
    (:predicates
        (at ?figure - figure ?file ?rank - location)
        (diff_by_Zero ?file ?rank - location)
        (diff_by_One ?file ?rank - location)
        (diff_by_Two ?file ?rank - location)
        (diff_by_Three ?file ?rank - location)
        (diff_by_Four ?file ?rank - location)
        (diff_by_Five ?file ?rank - location)
        (diff_by_Six ?file ?rank - location)
        (diff_by_Seven ?file ?rank - location)
        (diff_by_Eight ?file ?rank - location)
        (diff_by_N ?file ?rank - location)
        (plusOne_white ?file ?rank - location)
        (plusOne_black ?file ?rank - location)
        (pawn_double_white ?from_file ?to_file ?from_rank ?to_rank - location)
        (pawn_double_black ?from_file ?to_file ?from_rank ?to_rank - location)
    )

    (:action pawn_move_white
        :parameters (?pawn - pawn_w ?from_file ?from_rank ?to_file ?to_rank - location); ?figure - figure)
        :precondition (and 
                           (at ?pawn ?from_file ?from_rank)
                           (or    
                               (and ;single move:
                                   (diff_by_Zero ?from_file ?to_file) ; file +/- 0
                                   (plusOne_white ?from_rank ?to_rank) ; rank +1
                               )
                               (and ;double move:
                                   (pawn_double_white ?from_file ?to_file ?from_rank ?to_rank)
                               )
                           )
                           ;(not (at ?figure ?to_file ?to_rank))
                      )
        :effect (and (not (at ?pawn ?from_file ?from_rank))
                     (at ?pawn ?to_file ?to_rank)
                )
    )

    (:action pawn_move_black
        :parameters (?pawn - pawn_b ?from_file ?from_rank ?to_file ?to_rank - location); ?figure - figure)
        :precondition (and 
                           (at ?pawn ?from_file ?from_rank)
                           (or    
                               (and ;single move:
                                   (diff_by_Zero ?from_file ?to_file) ; file +/- 0
                                   (plusOne_black ?from_rank ?to_rank) ; rank +1
                               )
                               (and ;double move:
                                   (pawn_double_black ?from_file ?to_file ?from_rank ?to_rank)
                               )
                           )
                           ;(not (at ?figure ?to_file ?to_rank))
                      )
        :effect (and (not (at ?pawn ?from_file ?from_rank))
                     (at ?pawn ?to_file ?to_rank)
                )
    )

    (:action knight_move
        :parameters (?knight - knight ?from_file ?from_rank ?to_file ?to_rank - location); ?figure - figure)
        :precondition (and (at ?knight ?from_file ?from_rank)
                           (or
                               (and ;two files, one row:
                                   (diff_by_Two ?from_file ?to_file) ; file +/- 2
                                   (diff_by_One ?from_rank ?to_rank) ; rank +/- 1
                               )
                               (and ;two rows, one file:
                                   (diff_by_Two ?from_rank ?to_rank) ; rank +/- 2
                                   (diff_by_One ?from_file ?to_file) ; file +/- 1
                               )
                           )
                           ;(not (at ?figure ?to_file ?to_rank))
                      )
        :effect (and (not (at ?knight ?from_file ?from_rank))
                     (at ?knight ?to_file ?to_rank)
                )
    )

    (:action bishop_move
        :parameters (?bishop - bishop ?from_file ?from_rank ?to_file ?to_rank - location); ?figure - figure)
        :precondition (and (at ?bishop ?from_file ?from_rank)
                           ;diagonal moves (possibilities are "move 1 diagonally" up to "move n diagonally"):
                           (or
;[:action_bishop_move]
                           )
                           ;(not (at ?figure ?to_file ?to_rank))
                       )
        :effect (and (not (at ?bishop ?from_file ?from_rank))
                     (at ?bishop ?to_file ?to_rank)
                )
    )

    (:action rook_move
        :parameters (?rook - rook ?from_file ?from_rank ?to_file ?to_rank - location); ?figure - figure)
        :precondition (and (at ?rook ?from_file ?from_rank)
                           (or
                               (and ;vertical movement:
                                   (diff_by_Zero ?from_file ?to_file) ; file +/-0
                                   (diff_by_N ?from_rank ?to_rank) ; rank +/-n
                               )
                               (and ;horizontal movement:
                                   (diff_by_Zero ?from_rank ?to_rank) ; rank +/-0
                                   (diff_by_N ?from_file ?to_file) ; file +/-n
                               )
                           )
                           ;(not (at ?figure ?to_file ?to_rank))
                       )
        :effect (and (not (at ?rook ?from_file ?from_rank))
                     (at ?rook ?to_file ?to_rank)
                )
    )

    (:action queen_move
        :parameters (?queen - queen ?from_file ?from_rank ?to_file ?to_rank - location); ?figure - figure)
        :precondition (and (at ?queen ?from_file ?from_rank)
                           (or
                               ;diagonal moves (same as bishop moves):
                               (or
;[:action_queen_move]
                               )
                               ;horizontal & diagonal moves (same as rook moves):
                               (and ;vertical movement:
                                   (diff_by_Zero ?from_file ?to_file) ; file +/-0
                                   (diff_by_N ?from_rank ?to_rank) ; rank +/-n
                               )
                               (and ;horizontal movement:
                                   (diff_by_Zero ?from_rank ?to_rank) ; rank +/-0
                                   (diff_by_N ?from_file ?to_file) ; file +/-n
                               )
                           )
                           ;(not (at ?figure ?to_file ?to_rank))
                       )
        :effect (and (not (at ?queen ?from_file ?from_rank))
                     (at ?queen ?to_file ?to_rank)
                )
    )
    
    (:action king_move
        :parameters (?king - king ?from_file ?from_rank ?to_file ?to_rank - location); ?figure - figure)
        :precondition (and (at ?king ?from_file ?from_rank)
                           (diff_by_One ?from_file ?to_file) ; file +/-1
                           (diff_by_One ?from_rank ?to_rank) ; rank +/-1
                           ;(not (at ?figure ?to_file ?to_rank))
                       )
        :effect (and (not (at ?king ?from_file ?from_rank))
                     (at ?king ?to_file ?to_rank)
                )
    )
)