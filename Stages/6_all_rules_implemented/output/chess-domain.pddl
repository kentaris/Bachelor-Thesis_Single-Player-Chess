(define (domain chess)
    (:requirements :typing :equality :negative-preconditions :disjunctive-preconditions :derived-predicates)
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
        (pawn_start_pos_white ?from_file ?from_rank - location)
        (pawn_start_pos_black ?from_file ?from_rank - location)
        ;derived:
        (vert_reachable ?from_file ?from_rank ?to_file ?to_rank - location)
        (horiz_adj ?from_file ?from_rank ?to_file ?to_rank - location)
        (occupied ?file ?rank - location)
    )

    (:derived (occupied ?file ?rank - location) ;check if some figure is at location
        (exists(?figure - figure)
            (at ?figure ?file ?rank))
    )

    (:derived (vert_reachable ?from_file ?from_rank ?to_file ?to_rank - location) ;check if some figure is between from and to location
        (or (not(occupied ?to_file ?to_rank))
            (exists(?rank - location)
                 (and (not(=?from_rank ?rank))
                      (not(occupied ?to_file ?rank))
                      (diff_by_One ?from_file ?to_file)
                      (diff_by_Zero ?from_rank ?to_rank)
                      (vert_reachable ?from_file ?from_rank ?to_file ?to_rank)                      
                 )
            )
        )
    )

    (:action pawn_move_white
        :parameters (?pawn - pawn_w ?from_file ?from_rank ?to_file ?to_rank - location)
        :precondition (and 
                           (at ?pawn ?from_file ?from_rank)
                           (or    
                               (and ;single move:
                                   (not(occupied ?to_file ?to_rank))
                                   (diff_by_Zero ?from_file ?to_file) ; file +/- 0
                                   (plusOne_white ?from_rank ?to_rank) ; rank +1
                               )
                               (and ;double move:
                                   (pawn_start_pos_white ?from_file ?from_rank)
                                   (not(occupied ?to_file ?to_rank))
                                   (diff_by_Zero ?from_file ?to_file)
                                   (diff_by_Two ?from_rank ?to_rank)
                                   (vert_reachable ?from_file ?from_rank ?to_file ?to_rank)
                               )
                           )
                      )
        :effect (and (not (at ?pawn ?from_file ?from_rank))
                     (at ?pawn ?to_file ?to_rank)
                )
    )

    (:action pawn_move_black
        :parameters (?pawn - pawn_b ?from_file ?from_rank ?to_file ?to_rank - location)
        :precondition (and 
                           (at ?pawn ?from_file ?from_rank)
                           (or    
                               (and ;single move:
                                   (not(occupied ?to_file ?to_rank))
                                   (diff_by_Zero ?from_file ?to_file) ; file +/- 0
                                   (plusOne_black ?from_rank ?to_rank) ; rank +1
                               )
                               (and ;double move:
                                   (pawn_start_pos_black ?from_file ?from_rank)
                                   (not(occupied ?to_file ?to_rank))
                                   (diff_by_Zero ?from_file ?to_file)
                                   (diff_by_Two ?from_rank ?to_rank)
                                   (vert_reachable ?from_file ?from_rank ?to_file ?to_rank)
                               )
                           )
                      )
        :effect (and (not (at ?pawn ?from_file ?from_rank))
                     (at ?pawn ?to_file ?to_rank)
                )
    )

    (:action knight_move
        :parameters (?knight - knight ?from_file ?from_rank ?to_file ?to_rank - location)
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
                           (not(occupied ?to_file ?to_rank))
                      )
        :effect (and (not (at ?knight ?from_file ?from_rank))
                     (at ?knight ?to_file ?to_rank)
                )
    )

    (:action bishop_move
        :parameters (?bishop - bishop ?from_file ?from_rank ?to_file ?to_rank - location)
        :precondition (and (at ?bishop ?from_file ?from_rank)
                           ;diagonal moves (possibilities are "move 1 diagonally" up to "move n diagonally"):
                           (or
							   (and (diff_by_One ?from_file ?to_file) ;file +/-1
							        (diff_by_One ?from_rank ?to_rank) ;rank +/-1
							   )
							   (and (diff_by_Two ?from_file ?to_file) ;file +/-2
							        (diff_by_Two ?from_rank ?to_rank) ;rank +/-2
							   )
							   (and (diff_by_Three ?from_file ?to_file) ;file +/-3
							        (diff_by_Three ?from_rank ?to_rank) ;rank +/-3
							   )
							   (and (diff_by_Four ?from_file ?to_file) ;file +/-4
							        (diff_by_Four ?from_rank ?to_rank) ;rank +/-4
							   )
							   (and (diff_by_Five ?from_file ?to_file) ;file +/-5
							        (diff_by_Five ?from_rank ?to_rank) ;rank +/-5
							   )
                           )
                           (not(occupied ?to_file ?to_rank))
                       )
        :effect (and (not (at ?bishop ?from_file ?from_rank))
                     (at ?bishop ?to_file ?to_rank)
                )
    )

    (:action rook_move
        :parameters (?rook - rook ?from_file ?from_rank ?to_file ?to_rank - location)
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
                           (not(occupied ?to_file ?to_rank))
                       )
        :effect (and (not (at ?rook ?from_file ?from_rank))
                     (at ?rook ?to_file ?to_rank)
                )
    )

    (:action queen_move
        :parameters (?queen - queen ?from_file ?from_rank ?to_file ?to_rank - location)
        :precondition (and (at ?queen ?from_file ?from_rank)
                           (or
                               ;diagonal moves (same as bishop moves):
                               (or
								   (and (diff_by_One ?from_file ?to_file) ;file +/-1
								        (diff_by_One ?from_rank ?to_rank) ;rank +/-1
								   )
								   (and (diff_by_Two ?from_file ?to_file) ;file +/-2
								        (diff_by_Two ?from_rank ?to_rank) ;rank +/-2
								   )
								   (and (diff_by_Three ?from_file ?to_file) ;file +/-3
								        (diff_by_Three ?from_rank ?to_rank) ;rank +/-3
								   )
								   (and (diff_by_Four ?from_file ?to_file) ;file +/-4
								        (diff_by_Four ?from_rank ?to_rank) ;rank +/-4
								   )
								   (and (diff_by_Five ?from_file ?to_file) ;file +/-5
								        (diff_by_Five ?from_rank ?to_rank) ;rank +/-5
								   )
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
                           (not(occupied ?to_file ?to_rank))
                       )
        :effect (and (not (at ?queen ?from_file ?from_rank))
                     (at ?queen ?to_file ?to_rank)
                )
    )
    
    (:action king_move
        :parameters (?king - king ?from_file ?from_rank ?to_file ?to_rank - location)
        :precondition (and (at ?king ?from_file ?from_rank)
                           (diff_by_One ?from_file ?to_file) ; file +/-1
                           (diff_by_One ?from_rank ?to_rank) ; rank +/-1
                           (not(occupied ?to_file ?to_rank))
                       )
        :effect (and (not (at ?king ?from_file ?from_rank))
                     (at ?king ?to_file ?to_rank)
                )
    )
)