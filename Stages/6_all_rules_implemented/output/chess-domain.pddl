(define (domain chess)
    (:requirements :typing :equality :negative-preconditions :disjunctive-preconditions :derived-predicates :conditional-effects)
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
        ;normal predicates:
        (at ?figure - figure ?file ?rank - location)
        (not_moved ?figure - figure)
        ;(diff_by_N ?file ?rank - location)
        (diff_by_Zero ?file ?rank - location)
        (diff_by_One ?file ?rank - location)
        (diff_by_Two ?file ?rank - location)
        (plusOne ?file ?rank - location)
        (minusOne ?file ?rank - location)
        (pawn_start_pos_white ?from_file ?from_rank - location)
        (pawn_start_pos_black ?from_file ?from_rank - location)
        ;derived predicates:
        (occupied ?file ?rank - location)
        (occupied_by ?file ?rank - location ?color - color)
        (occupied_by_same_color ?file ?rank - location ?color - color)
        (occupied_by_figure ?figure - figure ?file ?rank - location)
        (empty ?file ?rank - location)
        (horiz_adj ?from_file ?from_rank ?to_file ?to_rank - location)
        (vert_adj ?from_file ?from_rank ?to_file ?to_rank - location)
        (diag_adj ?from_file ?from_rank ?to_file ?to_rank - location)
        (vert_reachable ?from_file ?from_rank ?to_file ?to_rank - location)
        (horiz_reachable ?from_file ?from_rank ?to_file ?to_rank - location)
        (diag_reachable ?from_file ?from_rank ?to_file ?to_rank - location)
        (vert_capturable ?from_file ?from_rank ?to_file ?to_rank - location ?color - color)
        (horiz_capturable ?from_file ?from_rank ?to_file ?to_rank - location ?color - color)
        (diag_capturable ?from_file ?from_rank ?to_file ?to_rank - location ?color - color)
        (castling_possible  ?king - king ?rank ?from_file_rook ?to_file_rook - location)
        (is_king_checked)
    )
;DERIVED PREDICATES:
 ;;;;;;;;;;;;;;;;;;;;
 ;Square info:
    (:derived (occupied ?file ?rank - location) ;check if some figure is at location
        (exists(?figure - figure)
            (at ?figure ?file ?rank)
        )
    )
    (:derived (occupied_by ?file ?rank - location ?color - color) ;check if some figure is at location with same color
        (exists(?figure - figure ?col - color)
            (and(at ?figure ?file ?rank)
                (=?color ?col)
            )
        )
    )
    (:derived (occupied_by_figure ?figure - figure ?file ?rank - location) ;check if some figure is at location with same color
        (at ?figure ?file ?rank)
    )
    (:derived (occupied_by_same_color ?file ?rank - location ?color - color) ;check if some figure is at location with same color
        (and
            (occupied ?file ?rank)
            (exists(?figure - figure ?col - color)
                (and(at ?figure ?file ?rank)
                    (=?color ?col)
                )
            )
        )
    )
    (:derived (empty ?file ?rank - location) ;checks if square is empty
        (not(exists(?figure - figure)
                   (at ?figure ?file ?rank)
            )
        )
    )

 ;adjacent:
    (:derived (vert_adj ?from_file ?from_rank ?to_file ?to_rank - location)
        (and(= ?from_file ?to_file) ;file +/-0
            (diff_by_One ?from_rank ?to_rank) ;rank +/-1
        )
    )
    (:derived (horiz_adj ?from_file ?from_rank ?to_file ?to_rank - location)
        (and(= ?from_rank ?to_rank) ;rank +/-0
            (diff_by_One ?from_file ?to_file) ;file +/-1
        )
    )
    (:derived (diag_adj ?from_file ?from_rank ?to_file ?to_rank - location)
        (and(diff_by_One ?from_rank ?to_rank) ;rank +/-1
            (diff_by_One ?from_file ?to_file) ;file +/-1
        )
    )

 ;rachable:
    (:derived (vert_reachable ?from_file ?from_rank ?to_file ?to_rank - location)
        (or (
                vert_adj ?from_file ?from_rank ?to_file ?to_rank
            )
            (exists(?next_rank - location)
                 (and (not(occupied ?to_file ?next_rank))
                      (not(=?from_rank ?next_rank)) ;don't stay on same rank
                      (diff_by_One ?from_rank ?next_rank) ;one step
                      (= ?from_file ?to_file) ;same file
                      (vert_reachable ?from_file ?next_rank ?to_file ?to_rank)
                 )
            )
        )
    )
    (:derived (horiz_reachable ?from_file ?from_rank ?to_file ?to_rank - location)
        (or (
                horiz_adj ?from_file ?from_rank ?to_file ?to_rank
            )
            (exists(?next_file - location)
                 (and (not(=?from_file ?to_file)) ;don't stay on same file
                      (= ?from_rank ?to_rank) ;file +/-0
                      (not(occupied ?next_file ?to_rank))
                      (diff_by_One ?from_file ?next_file) ;one step at a time
                      (horiz_reachable ?next_file ?from_rank ?to_file ?to_rank)
                 )
            )
        )
    )
    (:derived (diag_reachable ?from_file ?from_rank ?to_file ?to_rank - location)
        ;TODO: Bishops can move in zick-zack...
        (or (
                diag_adj ?from_file ?from_rank ?to_file ?to_rank
            )
            (exists(?next_file ?next_rank - location)
                   (and (not(=?from_file ?to_file)) ;don't stay on same file
                        (not(= ?from_rank ?to_rank)) ;don't stay on same rank
                        (not(occupied ?next_file ?next_rank))
                        (diff_by_One ?from_file ?next_file) ;one step at a time
                        (diff_by_One ?from_rank ?next_rank) ;one step at a time
                        ;ToDo: must be on same diagonal
                        (diag_reachable ?next_file ?next_rank ?to_file ?to_rank)
                        ;(diag_reachable ?from_file ?from_rank ?to_file ?to_rank) ;TODO: stay on same diagonal
                   )
            )
        )
    )

 ;capturable
    (:derived (diag_capturable ?from_file ?from_rank ?to_file ?to_rank - location ?color - color)
        (and (diff_by_One ?from_file ?to_file) ;file +/-1
			 (diff_by_One ?from_rank ?to_rank) ;rank +/-1
			 (not(exists(?figure - figure) ;there isn't a figure of same color at destination
			        (occupied_by ?to_file ?to_rank ?color)
                 )
             )
			 (not(empty ?to_file ?to_rank)) ;square also isn't empty (meaning black piece is on it)
        )
    )
    (:derived (vert_capturable ?from_file ?from_rank ?to_file ?to_rank - location ?color - color)
        (and (diff_by_One ?from_file ?to_file) ;file +/-1
			 (= ?from_rank ?to_rank) ;rank +/-1
			 (not(exists(?figure - figure) ;there isn't a figure of same color at destination
			        (occupied_by ?to_file ?to_rank ?color)
		         )
             )
			 (not(empty ?to_file ?to_rank)) ;square also isn't empty (meaning black piece is on it)
        )
    )
    (:derived (horiz_capturable ?from_file ?from_rank ?to_file ?to_rank - location ?color - color)
        (and (diff_by_One ?from_file ?to_file) ;file +/-1
			 (= ?from_rank ?to_rank) ;rank +/-1
			 (not(exists(?figure - figure) ;there isn't a figure of same color at destination
			        (occupied_by ?to_file ?to_rank ?color)
			     )
             )
			 (not(empty ?to_file ?to_rank)) ;square also isn't empty (meaning black piece is on it)
        )
    )
 ;king predicates:
    (:derived (castling_possible ?king - king ?rank ?from_file_rook ?to_file_rook - location)
        (or (occupied_by_figure ?king ?to_file_rook ?rank) ;move untill king is at position
            (and (diff_by_One ?from_file_rook ?to_file_rook) ;one step at a time
			     (occupied_by_figure ?king ?to_file_rook ?rank) ;necessary?
			     (castling_possible ?king ?rank ?from_file_rook ?to_file_rook)
            )
        )
    )
    ;(:derived (is_king_checked) ;TODO: include this in piece movements: piece can't move if it's own colored king is checked by moving
    ;    (exists (?from_file ?to_file - location ?king - king)
    ;            (and(at ?king ?from_file ?to_file)
    ;                
    ;            )
    ;    )
    ;)

;ACTIONS
 ;;;;;;;;
    (:action capture_by_white_pawn ;en passant white
        :parameters (?pawn - pawn_w ?color - color ?from_file ?from_rank ?to_file ?to_rank - location)
        :precondition (and
                           (occupied ?to_file ?to_rank)
                           (occupied_by_same_color ?to_file ?to_rank ?color)
                           (at ?pawn ?from_file ?from_rank)
                           (and ;diagonal capture:
                                (plusOne ?from_rank ?to_rank)
                                (diff_by_One ?from_file ?to_file)
                           )
                      )
        :effect (and (not (at ?pawn ?from_file ?from_rank))
                     (forall (?figure - figure)
                        (when (at ?figure ?to_file ?to_rank)
                            (not (at ?figure ?to_file ?to_rank))
                        )
                     )
                     (at ?pawn ?to_file ?to_rank)
                )
    )
    (:action pawn_move_white
        :parameters (?pawn - pawn_w ?from_file ?from_rank ?to_file ?to_rank - location)
        :precondition (and 
                           (= ?from_file ?to_file)
                           (at ?pawn ?from_file ?from_rank)
                           (not(occupied ?to_file ?to_rank))
                           (or    
                               (plusOne ?from_rank ?to_rank) ;single move
                               (and ;double move:
                                   (pawn_start_pos_white ?from_file ?from_rank)
                                   (diff_by_Two ?from_rank ?to_rank)
                                   (vert_reachable ?from_file ?from_rank ?to_file ?to_rank) ;we don't need to check if it's white or black here because as long as the diff is 2 and it is at starting pos, then the pawn can't go into the wrong direction because it would fall off the board 
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
                           (= ?from_file ?to_file) ; file +/- 0
                           (at ?pawn ?from_file ?from_rank)
                           (not(occupied ?to_file ?to_rank))
                           (or    
                               (minusOne ?from_rank ?to_rank) ;single move
                               (and ;double move:
                                   (pawn_start_pos_black ?from_file ?from_rank)
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
                           (not(occupied ?to_file ?to_rank))
                           (or
                               (and ;two files, one row:
                                   (diff_by_Two ?from_file ?to_file) ; file +/- 2
                                   (diff_by_One ?from_rank ?to_rank)) ; rank +/- 1
                               (and ;two rows, one file:
                                   (diff_by_Two ?from_rank ?to_rank) ; rank +/- 2
                                   (diff_by_One ?from_file ?to_file) ; file +/- 1
                               )
                           )
                       )
        :effect (and (not (at ?knight ?from_file ?from_rank))
                     (at ?knight ?to_file ?to_rank)
                )
    )
    
    (:action bishop_move
        :parameters (?bishop - bishop ?from_file ?from_rank ?to_file ?to_rank - location)
        :precondition (and (at ?bishop ?from_file ?from_rank)
                           (not(occupied ?to_file ?to_rank))
                           (diag_reachable ?from_file ?from_rank ?to_file ?to_rank)
                      )
        :effect (and (not (at ?bishop ?from_file ?from_rank))
                     (at ?bishop ?to_file ?to_rank)
                )
    )
    (:action rook_move
        :parameters (?rook - rook ?from_file ?from_rank ?to_file ?to_rank - location)
        :precondition (and (at ?rook ?from_file ?from_rank)
                           (not(occupied ?to_file ?to_rank))
                           (or (vert_reachable ?from_file ?from_rank ?to_file ?to_rank)
                               (horiz_reachable ?from_file ?from_rank ?to_file ?to_rank)
                           )
                      )
        :effect (and (not (at ?rook ?from_file ?from_rank))
                     (at ?rook ?to_file ?to_rank)
                     (not(not_moved ?rook))
                )
    )
    (:action queen_move
        :parameters (?queen - queen ?from_file ?from_rank ?to_file ?to_rank - location)
        :precondition (and (at ?queen ?from_file ?from_rank)
                           (not(occupied ?to_file ?to_rank))
                           (or (vert_reachable ?from_file ?from_rank ?to_file ?to_rank)
                               (horiz_reachable ?from_file ?from_rank ?to_file ?to_rank)
                               (diag_reachable ?from_file ?from_rank ?to_file ?to_rank)
                           )
                      )
        :effect (and (not (at ?queen ?from_file ?from_rank))
                     (at ?queen ?to_file ?to_rank)
                )
    )
    (:action king_move
        :parameters (?king - king ?from_file ?from_rank ?to_file ?to_rank - location)
        :precondition (and (at ?king ?from_file ?from_rank)
                           (not(occupied ?to_file ?to_rank))
                           (or 
                               (and ;diagonal move
                                   (diff_by_One ?from_file ?to_file)
                                   (diff_by_One ?from_rank ?to_rank)
                               )
                               (and ;vertical move
                                   (diff_by_One ?from_file ?to_file)
                                   (= ?from_rank ?to_rank)
                               )
                               (and ;horizontal move
                                   (= ?from_file ?to_file)
                                   (diff_by_One ?from_rank ?to_rank)
                               )
                           )
                      )
        :effect (and (not (at ?king ?from_file ?from_rank))
                     (at ?king ?to_file ?to_rank)
                     (not(not_moved ?king))
                )
    )

    (:action castling ;kingside & queenside
        :parameters (?king - king ?rook - rook ?from_rank ?to_rank ?from_file_rook ?to_file_rook ?from_file_king ?to_file_king - location)
        :precondition (and (not_moved ?king)
                           (not_moved ?rook)
                           (diff_by_Two ?from_file_king ?to_file_king)
                           (= ?from_rank ?to_rank) ;necessary?
                           ;Problem: the following line is always false somehow:
                           (castling_possible ?king ?to_rank ?from_file_rook ?to_file_rook)
                           (diff_by_One ?to_file_king ?to_file_rook) ;rook on the left or right of king
                      )
        :effect (and (not(at ?king ?from_file_king ?from_rank))
                     (not(at ?rook ?from_file_rook ?from_rank))
                     (at ?king ?to_file_king ?to_rank)
                     (at ?rook ?to_file_rook ?to_rank)
                     (not(not_moved ?king))
                     (not(not_moved ?rook))
                )
    )
)