(define (domain chess)
    (:requirements :typing :equality :negative-preconditions :disjunctive-preconditions :derived-predicates :conditional-effects)
    (:types
        figure - object
        location - object

        pawn knight bishop rook queen king - figure

        pawn_w   pawn_b   - pawn  
        knight_w knight_b - knight
        bishop_w bishop_b - bishop
        rook_w   rook_b   - rook  
        queen_w  queen_b  - queen 
        king_w   king_b   - king  
    )
    (:predicates
     ;static predicates:
        ;(diff_by_N ?file ?rank - location)
        (diff_by_One ?file ?rank - location)
        (diff_by_Two ?file ?rank - location)
        (diff_by_Three ?file ?rank - location)
        (plusOne ?file ?rank - location)
        (minusOne ?file ?rank - location)
        (minusOne_nTimes ?x ?y - location)
        (plusOne_nTimes ?x ?y - location)
        (pawn_start_pos_white ?from_file ?from_rank - location)
        (pawn_start_pos_black ?from_file ?from_rank - location)
        (is_white ?figure - figure)
        (is_black ?figure - figure)
        (is_pawn ?pawn - pawn)
        ;(is_knight ?knight - knight)
        ;(is_bishop ?bishop - bishop)
        ;(is_rook ?rook - rook)
        ;(is_queen ?queen - queen)
        ;(is_king ?king - king)
        (last_pawn_line ?to_file ?to_rank - location)
        (TRUE)
        (FALSE)
     ;fluent/normal predicates:
        (at ?figure - figure ?file ?rank - location)
        (not_moved ?figure - figure)
        (removed ?figure - figure)
        (white_s_turn)
        (last_piece_moved ?figure - figure)
        (double_moved ?pawn - pawn ?file ?rank - location)
        (empty_square ?file ?rank - location)
        ;(is_on_board ?figure - figure)

     ;derived predicates:
        (myturn ?figure - figure)
        (white_pawn_at ?file ?rank - location)
        (black_pawn_at ?file ?rank - location)

        (occupied_by_same_color ?figure - figure ?file ?rank - location)
        (same_color ?figure1 ?figure2 - figure)

        (horiz_adj ?from_file ?from_rank ?to_file ?to_rank - location)
        (vert_adj ?from_file ?from_rank ?to_file ?to_rank - location)
        (diag_adj ?from_file ?from_rank ?to_file ?to_rank - location)
        (same_diag ?from_file ?from_rank ?next_file ?next_rank ?to_file ?to_rank - location)
        (between ?from ?next ?to - location)

        (vert_reachable ?from_file ?from_rank ?to_file ?to_rank - location)
        (horiz_reachable ?from_file ?from_rank ?to_file ?to_rank - location)
        (diag_reachable ?from_file ?from_rank ?to_file ?to_rank - location)
        
        (vert_reachable_red ?file ?c_rank ?kt_rank - location)
        (horiz_reachable_red ?c_file ?rank ?kt_file - location)
        (diag_reachable_red ?from_file ?from_rank ?to_file ?to_rank - location)

        (king_to_rook_possible ?rook - rook ?rank ?from_file_king ?to_file_king - location)
        (kingside_rook ?rook - rook)
        (queenside_rook ?rook - rook)

        (red_zone ?figure - figure ?to_file ?to_rank - location)
        (move_through_red_zone ?king ?from_file_king ?rank ?to_file_king)

        (my_king_in_check ?figure - figure)
        (occupied_by_king ?file ?rank - location)
    )
;DERIVED PREDICATES:
 ;;;;;;;;;;;;;;;;;;;;
 ;figure info:
    (:derived (myturn ?figure - figure)
        (or 
            (and
                (is_white ?figure)
                (white_s_turn)
            )
            (and
                (is_black ?figure)
                (not(white_s_turn))
            )
        )
    )
    (:derived (white_pawn_at ?file ?rank - location)
        (exists(?figure - figure)
            (and
                (at ?figure ?file ?rank)
                (is_pawn ?figure)
                (is_white ?figure)
            )
        )
    )
    (:derived (black_pawn_at ?file ?rank - location)
        (exists(?figure - figure)
            (and
                (at ?figure ?file ?rank)
                (is_pawn ?figure)
                (is_black ?figure)
            )
        )
    )
 ;Square info:
    (:derived (same_color ?figure1 ?figure2 - figure)
        (or (and(is_white ?figure1)
                (is_white ?figure2)
            )
            (and(is_black ?figure1)
                (is_black ?figure2)
            )
        )
    )
    (:derived (occupied_by_same_color ?figure - figure ?file ?rank - location) ;check if some fig is at location with same color as the given figure
        (and
            (not(empty_square ?file ?rank))
            (exists(?fig - figure)
                (and(at ?fig ?file ?rank)
                    (or (and(is_white ?fig)
                            (is_white ?figure)
                        )
                        (and(is_black ?fig)
                            (is_black ?figure)
                        )
                    )
                )
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
    (:derived (same_diag ?from_file ?from_rank ?next_file ?next_rank ?to_file ?to_rank - location)
        (and
            (not(= ?from_file ?next_file))
            (not(= ?next_file ?to_file))
            (not(= ?from_rank ?next_rank))
            (not(= ?next_rank ?to_rank))
            (or ;rank +1/file+1
                (and(plusOne ?from_file ?next_file)
                    (plusOne_nTimes ?next_file ?to_file)
                    (plusOne ?from_rank ?next_rank)
                    (plusOne_nTimes ?next_rank ?to_rank)
                );rank-1/file-1
                (and(minusOne ?from_file ?next_file)
                    (minusOne_nTimes ?next_file ?to_file)
                    (minusOne ?from_rank ?next_rank)
                    (minusOne_nTimes ?next_rank ?to_rank)
                );rank-1/file+1
                (and(plusOne ?from_file ?next_file)
                    (plusOne_nTimes ?next_file ?to_file)
                    (minusOne ?from_rank ?next_rank)
                    (minusOne_nTimes ?next_rank ?to_rank)
                );rank+1/file-1
                (and(minusOne ?from_file ?next_file)
                    (minusOne_nTimes ?next_file ?to_file)
                    (plusOne ?from_rank ?next_rank)
                    (plusOne_nTimes ?next_rank ?to_rank)
                )
            )
        )
    )
    (:derived (between ?from ?next ?to - location)
        (and
            (not(= ?from ?next))
            (not(= ?next ?to))
            (or
                (and
                    (plusOne ?from ?next)
                    (plusOne_nTimes ?next ?to)
                )
                (and
                    (minusOne ?from ?next)
                    (minusOne_nTimes ?next ?to)
                )
            )
        )
    )
    (:derived (plusOne_nTimes ?x ?y - location)
        (or (plusOne ?x ?y)
            (exists(?next - location)
                (and(plusOne ?x ?next)
                    (plusOne_nTimes ?next ?y)
                )
            )
        )
    )
    (:derived (minusOne_nTimes ?x ?y - location)
        (or (minusOne ?x ?y)
            (exists(?next - location)
                (and(minusOne ?x ?next)
                    (minusOne_nTimes ?next ?y)
                )
            )
        )
    )
 ;rachable:
    (:derived (vert_reachable ?from_file ?from_rank ?to_file ?to_rank - location)
        (or (and(or
                   (empty_square ?to_file ?to_rank)
                   (and 
                        (not(empty_square ?to_file ?to_rank))
                        (exists(?figure - figure)
                            (and(at ?figure ?to_file ?to_rank)
                                (not(occupied_by_same_color ?figure ?from_file ?from_rank))
                            )
                        )
                   )
                )
                (vert_adj ?from_file ?from_rank ?to_file ?to_rank)
            )
            (exists(?next_rank - location)
                 (and 
                      (empty_square ?to_file ?next_rank)
                      ;(not(= ?from_rank ?next_rank)) ;don't stay on same rank
                      (diff_by_One ?from_rank ?next_rank) ;one step
                      (= ?from_file ?to_file) ;same file
                      (between ?from_rank ?next_rank ?to_rank)
                      (vert_reachable ?from_file ?next_rank ?to_file ?to_rank)
                 )
            )
        )
    )
    (:derived (horiz_reachable ?from_file ?from_rank ?to_file ?to_rank - location)
        (or (and(or
                   (empty_square ?to_file ?to_rank)
                   (and
                       (not(empty_square ?to_file ?to_rank))
                       (exists(?figure - figure)
                           (and(at ?figure ?to_file ?to_rank)
                               (not(occupied_by_same_color ?figure ?from_file ?from_rank))
                           )
                       )
                   )
                )
                (horiz_adj ?from_file ?from_rank ?to_file ?to_rank)
            )
            (exists(?next_file - location)
                 (and 
                      (empty_square ?next_file ?to_rank)
                      ;(not(= ?from_file ?to_file)) ;don't stay on same file
                      (diff_by_One ?from_file ?next_file) ;one step at a time
                      (= ?from_rank ?to_rank) ;same rank
                      (between ?from_file ?next_file ?to_file)
                      (horiz_reachable ?next_file ?from_rank ?to_file ?to_rank)
                 )
            )
        )
    )
    (:derived (diag_reachable ?from_file ?from_rank ?to_file ?to_rank - location)
        (or (and(or
                   (empty_square ?to_file ?to_rank)
                   (and
                       (not(empty_square ?to_file ?to_rank))
                       (exists(?figure - figure)
                           (and(at ?figure ?to_file ?to_rank)
                               (not(occupied_by_same_color ?figure ?from_file ?from_rank))
                           )
                       )
                   )
                )
                (diag_adj ?from_file ?from_rank ?to_file ?to_rank)
            )
            (exists(?next_file ?next_rank - location)
                (and (not(= ?from_file ?to_file))
                     (not(= ?from_rank ?to_rank))
                     ;(not(= ?from_file ?next_file)) ;don't stay on same file
                     ;(not(= ?from_rank ?next_rank)) ;don't stay on same rank
                     (diff_by_One ?from_file ?next_file) ;one step at a time
                     (diff_by_One ?from_rank ?next_rank) ;one step at a time
                     (empty_square ?next_file ?next_rank)
                     (same_diag ?from_file ?from_rank ?next_file ?next_rank ?to_file ?to_rank) ;bishop needs to stay on the same diagonal
                     (diag_reachable ?next_file ?next_rank ?to_file ?to_rank)
                )
            )
        )
    )
    (:derived (vert_reachable_red ?file ?c_rank ?kt_rank - location)
        (or 
            (and
                (vert_adj ?file ?c_rank ?file ?kt_rank)
            )
            (exists(?next_rank - location)
                 (and 
                      ;(not(= ?c_rank ?next_rank)) ;don't stay on same rank
                      (diff_by_One ?c_rank ?next_rank) ;one step
                      (between ?c_rank ?next_rank ?kt_rank)
                      (empty_square ?file ?next_rank) ;path not blocked
                      (vert_reachable_red ?file ?next_rank ?kt_rank)
                 )
            )
        )
    )
    (:derived (horiz_reachable_red ?c_file ?rank ?kt_file - location)
        (or
            (and
                (vert_adj ?c_file ?rank ?kt_file ?rank)
            )
            (exists(?next_file - location)
                 (and 
                      ;(not(= ?c_file ?next_file)) ;don't stay on same rank
                      (diff_by_One ?c_file ?next_file) ;one step
                      (between ?c_file ?next_file ?kt_file)
                      (empty_square ?next_file ?rank) ;path not blocked
                      (horiz_reachable_red ?next_file ?rank ?kt_file)
                 )
            )
        )
    )
    (:derived (diag_reachable_red ?c_file ?c_rank ?kt_file ?kt_rank - location)
        (or (diag_adj ?c_file ?c_rank ?kt_file ?kt_rank)
            (exists(?next_file ?next_rank - location)
                (and ;(not(= ?c_file ?next_file)) ;don't stay on same file
                     ;(not(= ?c_rank ?next_rank)) ;don't stay on same rank
                     (diff_by_One ?c_file ?next_file) ;one step at a time
                     (diff_by_One ?c_rank ?next_rank) ;one step at a time
                     (empty_square ?next_file ?next_rank)
                     (same_diag ?c_file ?c_rank ?next_file ?next_rank ?kt_file ?kt_rank) ;bishop needs to stay on the same diagonal
                     (diag_reachable_red ?next_file ?next_rank ?kt_file ?kt_rank)
                )
            )
        )
    )
    
 ;king predicates:
    (:derived (my_king_in_check ?figure)
        ;(FALSE)   ; TODO: check....
        (exists (?king - king ?k_file ?k_rank - location)
            (and
                (at ?king ?k_file ?k_rank)
                (same_color ?figure ?king) ;king must be of the same color
                (red_zone ?king ?k_file ?k_rank) ;is the current king position a red zone?
            )
        )
    )
    (:derived (occupied_by_king ?file ?rank)
        (exists (?king - king) 
            (at ?king ?file ?rank)
        )        
    )
    (:derived (move_through_red_zone ?king ?from_file_king ?rank ?to_file_king)
        (or ;TODO: TEST it  '5/5/5/5/RK2R','5/5/5/5/R1RK1'
            (and
                (not(red_zone ?king ?to_file_king ?rank))
                (horiz_adj ?from_file_king ?rank ?to_file_king ?rank)
            )
            (exists(?next_file - location)
                 (and 
                      (empty_square ?next_file ?rank)
                      ;(not(= ?from_file_king ?to_file_king)) ;don't stay on same file
                      (diff_by_One ?from_file_king ?next_file) ;one step at a time
                      (between ?from_file_king ?next_file ?to_file_king)
                      (horiz_reachable ?next_file ?rank ?to_file_king ?rank)
                      (not(red_zone ?king ?to_file_king ?rank))
                      (move_through_red_zone ?king ?next_file ?rank ?to_file_king)
                 )
            )
        )
    )
    (:derived (red_zone ?king - king ?kt_file ?kt_rank - location) ;for some figure (capturer) on the board: is there a king of opposite color it can move to?    
        (or 
            (exists(?pawn - pawn ?c_file ?c_rank - location)
                (and;(is_pawn ?pawn)
                    ;(is_on_board ?pawn)
                    (at ?pawn ?c_file ?c_rank)
                    (diag_adj ?c_file ?c_rank ?kt_file ?kt_rank)
                    (diff_by_One ?kt_file ?c_file) ;diagonal capure
                    (not(= ?c_file ?kt_file))
                    (not(= ?c_rank ?kt_rank))
                    (not(same_color ?king ?pawn))
                    (and
                        (or 
                            (and
                                (is_black ?pawn)
                                (is_white ?king)
                                (minusOne ?c_rank ?kt_rank)
                            )
                            (and
                                (is_white ?pawn)
                                (is_black ?king)
                                (plusOne ?c_rank ?kt_rank)
                            )
                        )
                    )              
                )
            )
            (exists(?knight - knight ?c_file ?c_rank - location)
                (and;(is_knight ?knight)
                    ;(is_on_board ?knight)
                    (at ?knight ?c_file ?c_rank)
                    (not(= ?c_file ?kt_file)) ;kt_file & kt_rank should = landing position for the knight
                    (not(= ?c_rank ?kt_rank))
                    (not(same_color ?king ?knight));king cannot be checked by his own pieces:
                    (or
                        (and ;two files, one row:
                            (diff_by_Two ?c_file ?kt_file) ; file +/- 2
                            (diff_by_One ?c_rank ?kt_rank)) ; rank +/- 1
                        (and ;two rows, one file:
                            (diff_by_Two ?c_rank ?kt_rank) ; rank +/- 2
                            (diff_by_One ?c_file ?kt_file) ; file +/- 1
                        )
                    )
                )
            )
            (exists(?king2 - king ?c_file ?c_rank - location) ;kings can't move near each other
                (and;(is_king ?king2)
                    ;(is_on_board ?king2)
                    (at ?king2 ?c_file ?c_rank)
                    ;(not(myturn ?king2))
                    (not(same_color ?king ?king2))
                    (and
                        (or 
                            (and ;diagonal move
                                (diff_by_One ?c_file ?kt_file)
                                (diff_by_One ?c_rank ?kt_rank)
                            )
                            (and ;vertical move
                                (= ?c_rank ?kt_rank)
                                (diff_by_One ?c_file ?kt_file)
                            )
                            (and ;horizontal move
                                (= ?c_file ?kt_file)
                                (diff_by_One ?c_rank ?kt_rank)
                            )
                        )
                    )
                )
            )
            (exists(?rook - rook ?c_file ?c_rank - location)
                (and;(is_rook ?rook)
                    ;(is_on_board ?rook)
                    (at ?rook ?c_file ?c_rank) ;valid starting location
                    (not(same_color ?king ?rook));king cannot be checked by his own pieces:
                    ;can the king be reached by the capturer:
                    (or 
                        (and
                            (= ?kt_file ?c_file) ;vertical movement
                            (vert_reachable_red ?c_file ?c_rank ?kt_rank)
                        )
                        (and
                            (= ?kt_rank ?c_rank) ;horizontal movement
                            (horiz_reachable_red ?c_file ?c_rank ?kt_file)
                        )
                    )
                )
            )
            (exists(?bishop - bishop ?c_file ?c_rank  - location)
                (and;(is_bishop ?bishop)
                    ;(is_on_board ?bishop)
                    (at ?bishop ?c_file ?c_rank)
                    (not(same_color ?king ?bishop));king cannot be checked by his own pieces:
                    ;(not(= ?c_file ?kt_file))
                    ;(not(= ?c_rank ?kt_rank))
                    (diag_reachable_red ?c_file ?c_rank ?kt_file ?kt_rank)
                )
            )
            (exists(?queen - queen ?c_file ?c_rank - location)
                (and;(is_queen ?queen)
                    ;(is_on_board ?queen)
                    (at ?queen ?c_file ?c_rank)
                    (not(same_color ?king ?queen));king cannot be checked by his own pieces:
                    ;can the king be reached by the capturer:
                    (or 
                        (and
                            (= ?kt_file ?c_file) ;vertical movement
                            (not(= ?kt_rank ?c_rank))
                            (vert_reachable_red ?c_file ?c_rank ?kt_rank)
                        )
                        (and
                            (= ?kt_rank ?c_rank) ;horizontal movement
                            (not(= ?kt_file ?c_file))
                            (horiz_reachable_red ?c_file ?c_rank ?kt_file)
                        )
                        (and ;diagonal movement
                            (not(= ?c_file ?kt_file))
                            (not(= ?c_rank ?kt_rank))
                            (diag_reachable_red ?c_file ?c_rank ?kt_file ?kt_rank)
                        )
                    )
                )
            )
        )
    )
;ACTIONS
 ;;;;;;;;
  ;PAWN Actions
    (:action en_passant
        :parameters (?pawn - pawn ?from_file ?from_rank ?to_file ?to_rank - location)
        :precondition (and  
                          (exists(?pawn2 - pawn ?file ?rank - location)
                              (and
                                  ;(is_on_board ?pawn2)
                                  (double_moved ?pawn2 ?file ?rank) ;we also need to check if the double move happend in ONLY the last turn. I do this by also saving the location of the pawn in the double_moved predicate and by saving the last piece that moved
                                  (last_piece_moved ?pawn2)
                                  (at ?pawn2 ?file ?rank)
                                  (not(same_color ?pawn2 ?pawn))
                                  (diff_by_One ?from_file ?file) ;left or right of me
                                  (= ?from_rank ?rank) ;pawn is next to me
                                  (or
                                      (and
                                          (plusOne ?to_rank ?rank) ;white pawns move up the board only
                                          (is_black ?pawn)
                                      )
                                      (and
                                          (minusOne ?to_rank ?rank) ;black pawns move down the board only
                                          (is_white ?pawn)
                                      )
                                  )
                              )
                          )
                          (or
                              (and
                                  (plusOne ?from_rank ?to_rank) ;white pawns move up the board only
                                  (is_white ?pawn)
                              )
                              (and
                                  (minusOne ?from_rank ?to_rank) ;black pawns move down the board only
                                  (is_black ?pawn)
                              )
                          )
                          (at ?pawn ?from_file ?from_rank)
                          (diff_by_One ?from_file ?to_file)
                          (diff_by_One ?from_rank ?to_rank)
                          (not(= ?from_file ?to_file))
                          (not(= ?from_rank ?to_rank))
                          (myturn ?pawn)
                          (not(my_king_in_check ?pawn))
                      )
        :effect (and 
                     (not (at ?pawn ?from_file ?from_rank))
                     (forall (?pawn2 - pawn ?file ?rank - location)
                        (when (and  
                                  (at ?pawn2 ?file ?rank)
                                  (double_moved ?pawn2 ?file ?rank)
                                  (last_piece_moved ?pawn2)
                                  (diff_by_One ?from_file ?file) ;left or right of me
                                  (= ?from_rank ?rank) ;pawn is next to me
                                  (or
                                      (and
                                          (plusOne ?to_rank ?rank) ;white pawns move up the board only
                                          (is_black ?pawn)
                                      )
                                      (and
                                          (minusOne ?to_rank ?rank) ;black pawns move down the board only
                                          (is_white ?pawn)
                                      )
                                  )
                                  (not(same_color ?pawn ?pawn2))) ;capturable piece = opposite color
                            (and
                                (not (at ?pawn2 ?file ?rank))
                                (removed ?pawn2)
                                (empty_square ?file ?rank)
                                ;(not(is_on_board ?pawn2))
                            )
                        )
                     )
                     (at ?pawn ?to_file ?to_rank)
                     (last_piece_moved ?pawn)
                     (when (white_s_turn)
                        (not(white_s_turn))
                     )
                     (when (not(white_s_turn))
                        (white_s_turn)
                     )
                     (not(empty_square ?to_file ?to_rank))
                     (empty_square ?from_file ?from_rank)
                )
    )
    (:action pawn_promotion_queen ;TODO: test extra pieces added
        :parameters (?queen - queen ?from_file ?from_rank ?to_file ?to_rank - location)
        :precondition (and
                          (myturn ?queen)
                          ;(not(is_on_board ?queen))
                          (exists (?pawn - pawn)
                                (and
                                    (last_pawn_line ?to_file ?to_rank)
                                    (at ?pawn ?from_file ?from_rank)
                                    (empty_square ?to_file ?to_rank)
                                    (same_color ?pawn ?queen)
                                    (or
                                        (and ;pawn moves forward into promotion
                                            (= ?from_file ?to_file)
                                            (diff_by_One ?from_rank ?to_rank)
                                        )
                                        (and ;pawn captures into promotion
                                            (diff_by_One ?from_file ?to_file)
                                            (diff_by_One ?from_rank ?to_rank)
                                            (not(= ?from_file ?to_file))
                                            (not(= ?from_rank ?to_rank))
                                            (not(empty_square ?to_file ?to_rank))
                                            (not(occupied_by_same_color ?pawn ?to_file ?to_rank)) ;it is not my own color
                                        )
                                    )
                                    (and
                                         (or(and(plusOne ?from_rank ?to_rank) ;white pawns move up the board only
                                                (is_white ?pawn)
                                            )
                                            (and(minusOne ?from_rank ?to_rank) ;black pawns move down the board only
                                                (is_black ?pawn)
                                            )
                                         )
                                    )
                                )
                          )
                          (not(my_king_in_check ?queen))
                      )
        :effect (and 
                    (at ?queen ?to_file ?to_rank)
                    (forall (?pawn - pawn)
                        (when
                            (and
                                (at ?pawn ?from_file ?from_rank)
                                (same_color ?pawn ?queen)
                            )
                            (not(at ?pawn ?from_file ?from_rank))
                        )
                    )
                    (not(empty_square ?to_file ?to_rank))
                    (empty_square ?from_file ?from_rank)
                    (when (white_s_turn)
                       (not(white_s_turn))
                    )
                    (when (not(white_s_turn))
                       (white_s_turn)
                    )
                    (last_piece_moved ?queen)
                    ;(is_on_board ?queen)
                )
    )
    (:action pawn_promotion_rook
        :parameters (?rook - rook ?from_file ?from_rank ?to_file ?to_rank - location)
        :precondition (and
                          (myturn ?rook)
                          ;(not(is_on_board ?rook))
                          (exists (?pawn - pawn)
                                (and
                                    (last_pawn_line ?to_file ?to_rank)
                                    (at ?pawn ?from_file ?from_rank)
                                    (empty_square ?to_file ?to_rank)
                                    (same_color ?pawn ?rook)
                                    (or
                                        (and ;pawn moves forward into promotion
                                            (= ?from_file ?to_file)
                                            (diff_by_One ?from_rank ?to_rank)
                                        )
                                        (and ;pawn captures into promotion
                                            (diff_by_One ?from_file ?to_file)
                                            (diff_by_One ?from_rank ?to_rank)
                                            (not(= ?from_file ?to_file))
                                            (not(= ?from_rank ?to_rank))
                                            (not(empty_square ?to_file ?to_rank))
                                            (not(occupied_by_same_color ?pawn ?to_file ?to_rank)) ;it is not my own color
                                        )
                                    )
                                    (and
                                         (or(and(plusOne ?from_rank ?to_rank) ;white pawns move up the board only
                                                (is_white ?pawn)
                                            )
                                            (and(minusOne ?from_rank ?to_rank) ;black pawns move down the board only
                                                (is_black ?pawn)
                                            )
                                         )
                                    )
                                )
                          )
                          (not(my_king_in_check ?rook))
                      )
        :effect (and 
                    (at ?rook ?to_file ?to_rank)
                    (forall (?pawn - pawn)
                        (when
                            (and
                                (at ?pawn ?from_file ?from_rank)
                                (same_color ?pawn ?rook)
                            )
                            (not(at ?pawn ?from_file ?from_rank))
                        )
                    )
                    (not(empty_square ?to_file ?to_rank))
                    (empty_square ?from_file ?from_rank)
                    (when (white_s_turn)
                       (not(white_s_turn))
                    )
                    (when (not(white_s_turn))
                       (white_s_turn)
                    )
                    (last_piece_moved ?rook)
                    ;(is_on_board ?rook)
                )
    )
    (:action pawn_promotion_knight
        :parameters (?knight - knight ?from_file ?from_rank ?to_file ?to_rank - location)
        :precondition (and
                          (myturn ?knight)
                          ;(not(is_on_board ?knight))
                          (exists (?pawn - pawn)
                                (and
                                    (last_pawn_line ?to_file ?to_rank)
                                    (at ?pawn ?from_file ?from_rank)
                                    (empty_square ?to_file ?to_rank)
                                    (same_color ?pawn ?knight)
                                    (or
                                        (and ;pawn moves forward into promotion
                                            (= ?from_file ?to_file)
                                            (diff_by_One ?from_rank ?to_rank)
                                        )
                                        (and ;pawn captures into promotion
                                            (diff_by_One ?from_file ?to_file)
                                            (diff_by_One ?from_rank ?to_rank)
                                            (not(= ?from_file ?to_file))
                                            (not(= ?from_rank ?to_rank))
                                            (not(empty_square ?to_file ?to_rank))
                                            (not(occupied_by_same_color ?pawn ?to_file ?to_rank)) ;it is not my own color
                                        )
                                    )
                                    (and
                                         (or(and(plusOne ?from_rank ?to_rank) ;white pawns move up the board only
                                                (is_white ?pawn)
                                            )
                                            (and(minusOne ?from_rank ?to_rank) ;black pawns move down the board only
                                                (is_black ?pawn)
                                            )
                                         )
                                    )
                                )
                          )
                          (not(my_king_in_check ?knight))
                      )
        :effect (and 
                    (at ?knight ?to_file ?to_rank)
                    (forall (?pawn - pawn)
                        (when
                            (and
                                (at ?pawn ?from_file ?from_rank)
                                (same_color ?pawn ?knight)
                            )
                            (not(at ?pawn ?from_file ?from_rank))
                        )
                    )
                    (not(empty_square ?to_file ?to_rank))
                    (empty_square ?from_file ?from_rank)
                    (when (white_s_turn)
                       (not(white_s_turn))
                    )
                    (when (not(white_s_turn))
                       (white_s_turn)
                    )
                    (last_piece_moved ?knight)
                    ;(is_on_board ?knight)
                )
    )
    (:action pawn_promotion_bishop
        :parameters (?bishop - bishop ?from_file ?from_rank ?to_file ?to_rank - location)
        :precondition (and
                          (myturn ?bishop)
                          ;(not(is_on_board ?bishop))
                          (exists (?pawn - pawn)
                                (and
                                    (last_pawn_line ?to_file ?to_rank)
                                    (at ?pawn ?from_file ?from_rank)
                                    (empty_square ?to_file ?to_rank)
                                    (same_color ?pawn ?bishop)
                                    (or
                                        (and ;pawn moves forward into promotion
                                            (= ?from_file ?to_file)
                                            (diff_by_One ?from_rank ?to_rank)
                                        )
                                        (and ;pawn captures into promotion
                                            (diff_by_One ?from_file ?to_file)
                                            (diff_by_One ?from_rank ?to_rank)
                                            (not(= ?from_file ?to_file))
                                            (not(= ?from_rank ?to_rank))
                                            (not(empty_square ?to_file ?to_rank))
                                            (not(occupied_by_same_color ?pawn ?to_file ?to_rank)) ;it is not my own color
                                        )
                                    )
                                    (and
                                         (or(and(plusOne ?from_rank ?to_rank) ;white pawns move up the board only
                                                (is_white ?pawn)
                                            )
                                            (and(minusOne ?from_rank ?to_rank) ;black pawns move down the board only
                                                (is_black ?pawn)
                                            )
                                         )
                                    )
                                )
                          )
                          (not(my_king_in_check ?bishop))
                      )
        :effect (and 
                    (at ?bishop ?to_file ?to_rank)
                    (forall (?pawn - pawn)
                        (when
                            (and
                                (at ?pawn ?from_file ?from_rank)
                                (same_color ?pawn ?bishop)
                            )
                            (not(at ?pawn ?from_file ?from_rank))
                        )
                    )
                    (not(empty_square ?to_file ?to_rank))
                    (empty_square ?from_file ?from_rank)
                    (when (white_s_turn)
                       (not(white_s_turn))
                    )
                    (when (not(white_s_turn))
                       (white_s_turn)
                    )
                    (last_piece_moved ?bishop)
                    ;(is_on_board ?bishop)
                )
    )
    (:action pawn_capture
        :parameters (?pawn - pawn ?from_file ?from_rank ?to_file ?to_rank - location)
        :precondition (and
                           ;(is_pawn ?pawn)
                           (at ?pawn ?from_file ?from_rank)
                           ;(is_on_board ?pawn)
                           (diff_by_One ?from_file ?to_file)
                           (diff_by_One ?from_rank ?to_rank)
                           (not(= ?from_file ?to_file))
                           (not(= ?from_rank ?to_rank))
                           (myturn ?pawn)
                           (not(empty_square ?to_file ?to_rank))
                           (not(occupied_by_same_color ?pawn ?to_file ?to_rank)) ;it is not my own color
                           (and ;diagonal capture:
                                (or(and(plusOne ?from_rank ?to_rank) ;white pawns move up the board only
                                       (is_white ?pawn)
                                   )
                                   (and(minusOne ?from_rank ?to_rank) ;black pawns move down the board only
                                       (is_black ?pawn)
                                   )
                                )
                           )
                           (not(my_king_in_check ?pawn))
                      )
        :effect (and 
                     (not (at ?pawn ?from_file ?from_rank))
                     (forall (?figure - figure)
                       (and
                        (when 
                            (and
                                (at ?figure ?to_file ?to_rank) ;ADL conditional effect
                                (not(same_color ?pawn ?figure)) ;capturable piece = opposite color
                            )
                            (and
                                (not (at ?figure ?to_file ?to_rank))
                                (removed ?figure)
                                ;(not(is_on_board ?figure))
                            )
                        )
                       )
                     )
                     (at ?pawn ?to_file ?to_rank)
                     (last_piece_moved ?pawn)
                     (when (white_s_turn)
                        (not(white_s_turn))
                     )
                     (when (not(white_s_turn))
                        (white_s_turn)
                     )
                     (empty_square ?from_file ?from_rank)
                     (not(empty_square ?to_file ?to_rank))
                )
    )
    (:action pawn_move_one
        :parameters (?pawn - pawn ?from_file ?from_rank ?to_file ?to_rank - location)
        :precondition (and 
                           ;(is_pawn ?pawn)
                           (not(last_pawn_line ?to_file ?to_rank))
                           ;(is_on_board ?pawn)
                           (at ?pawn ?from_file ?from_rank)
                           ;(not(at ?pawn ?to_file ?to_rank))
                           (not(= ?from_rank ?to_rank))
                           (= ?from_file ?to_file)
                           (empty_square ?to_file ?to_rank)
                           (diff_by_One ?from_rank ?to_rank)
                           (myturn ?pawn)
                           (or
                               (and
                                   (plusOne ?from_rank ?to_rank) ;single move white
                                   (is_white ?pawn)
                               )
                               (and
                                   (minusOne ?from_rank ?to_rank) ;single move black
                                   (is_black ?pawn)
                               )
                           )
                           (not(my_king_in_check ?pawn))
                       )
        :effect (and 
                     (at ?pawn ?to_file ?to_rank)
                     (not (at ?pawn ?from_file ?from_rank))
                     (last_piece_moved ?pawn)
                     (when (white_s_turn)
                        (not(white_s_turn))
                     )
                     (when (not(white_s_turn))
                        (white_s_turn)
                     )
                     (empty_square ?from_file ?from_rank)
                     (not(empty_square ?to_file ?to_rank))
                )
    )
    (:action pawn_move_two
        :parameters (?pawn - pawn ?from_file ?from_rank ?to_file ?to_rank - location)
        :precondition (and 
                           (not(double_moved ?pawn ?from_file ?from_rank))
                           ;(is_on_board ?pawn)
                           ;(is_pawn ?pawn)
                           (at ?pawn ?from_file ?from_rank)
                           (not(= ?from_rank ?to_rank))
                           (= ?from_file ?to_file)
                           (empty_square ?to_file ?to_rank)
                           (myturn ?pawn)
                           (or 
                               (and ;double move white:
                                   (is_white ?pawn)
                                   (pawn_start_pos_white ?from_file ?from_rank)
                                   (diff_by_Two ?from_rank ?to_rank) ;we don't need to check if it's white or black here because as long as the diff is 2 and it is at starting pos, then the pawn can't go into the wrong direction because it would fall off the board 
                                   (vert_reachable ?from_file ?from_rank ?to_file ?to_rank) 
                               )
                               (and ;double move black:
                                    (is_black ?pawn)
                                    (pawn_start_pos_black ?from_file ?from_rank)
                                    (diff_by_Two ?from_rank ?to_rank) ;we don't need to check if it's white or black here because as long as the diff is 2 and it is at starting pos, then the pawn can't go into the wrong direction because it would fall off the board 
                                    (vert_reachable ?from_file ?from_rank ?to_file ?to_rank)
                               )
                           )
                           (not(my_king_in_check ?pawn))
                       )
        :effect (and 
                     (not (at ?pawn ?from_file ?from_rank))
                     (at ?pawn ?to_file ?to_rank)
                     (last_piece_moved ?pawn)
                     (double_moved ?pawn ?to_file ?to_rank)
                     (when (white_s_turn)
                        (not(white_s_turn))
                     )
                     (when (not(white_s_turn))
                        (white_s_turn)
                     )
                     (empty_square ?from_file ?from_rank)
                     (not(empty_square ?to_file ?to_rank))
                )
    )
  ;other actions
    (:action knight_move
        :parameters (?knight - knight ?from_file ?from_rank ?to_file ?to_rank - location)
        :precondition (and ;(is_knight ?knight)
                           (at ?knight ?from_file ?from_rank)
                           ;(is_on_board ?knight)
                           (not(= ?from_file ?to_file))
                           (not(= ?from_rank ?to_rank))
                           (myturn ?knight)
                           (or
                               (and ;two files, one row:
                                   (diff_by_Two ?from_file ?to_file) ; file +/- 2
                                   (diff_by_One ?from_rank ?to_rank)) ; rank +/- 1
                               (and ;two rows, one file:
                                   (diff_by_Two ?from_rank ?to_rank) ; rank +/- 2
                                   (diff_by_One ?from_file ?to_file) ; file +/- 1
                               )
                           )
                           (or
                              (empty_square ?to_file ?to_rank)
                              (not(occupied_by_same_color ?knight ?to_file ?to_rank)) ;capturable piece = opposite color
                           )
                           (not(my_king_in_check ?knight))
                       )
        :effect (and 
                     (not (at ?knight ?from_file ?from_rank))
                     (forall (?figure - figure)
                        (when 
                            (and
                                (at ?figure ?to_file ?to_rank)
                                (not(same_color ?knight ?figure)) ;capturable piece = opposite color
                            )
                            (and
                                (not (at ?figure ?to_file ?to_rank))
                                (removed ?figure)
                                ;(not(is_on_board ?figure))
                            )
                        )
                     )
                     (at ?knight ?to_file ?to_rank)
                     (when (white_s_turn)
                        (not(white_s_turn))
                     )
                     (when (not(white_s_turn))
                        (white_s_turn)
                     )
                     (empty_square ?from_file ?from_rank)
                     (not(empty_square ?to_file ?to_rank))
                     (last_piece_moved ?knight)
                )
    )    
    (:action bishop_move
        :parameters (?bishop - bishop ?from_file ?from_rank ?to_file ?to_rank - location)
        :precondition (and;(is_bishop ?bishop)
                          (at ?bishop ?from_file ?from_rank)
                          ;(is_on_board ?bishop)
                          ;(not(at ?bishop ?to_file ?to_rank))
                          (not(= ?from_file ?to_file))
                          (not(= ?from_rank ?to_rank))
                          (myturn ?bishop)
                          (or ;no piece at destination:
                             (empty_square ?to_file ?to_rank)
                             ;capturable piece at destination:
                             (and
                                 (not(empty_square ?to_file ?to_rank))
                                 (not(occupied_by_same_color ?bishop ?to_file ?to_rank)) ;capturable piece = opposite color
                             )
                          )
                          (diag_reachable ?from_file ?from_rank ?to_file ?to_rank)
                          (not(my_king_in_check ?bishop))
                      )
        :effect (and 
                     (not (at ?bishop ?from_file ?from_rank))
                     (forall (?figure - figure)
                        (when
                            (and
                                (at ?figure ?to_file ?to_rank)
                                (not(same_color ?bishop ?figure)) ;capturable piece = opposite color
                            )
                            (and
                                (not (at ?figure ?to_file ?to_rank))
                                (removed ?figure)
                                ;(not(is_on_board ?figure))
                            )
                        )
                     )
                     (at ?bishop ?to_file ?to_rank)
                     (when (white_s_turn)
                        (not(white_s_turn))
                     )
                     (when (not(white_s_turn))
                        (white_s_turn)
                     )
                     (empty_square ?from_file ?from_rank)
                     (not(empty_square ?to_file ?to_rank))
                     (last_piece_moved ?bishop)
                )
    )
    (:action rook_move
        :parameters (?rook - rook ?from_file ?from_rank ?to_file ?to_rank - location)
        :precondition (and;(is_rook ?rook)
                          (at ?rook ?from_file ?from_rank)
                          ;(is_on_board ?rook)
                          (or ;no piece at destination:
                             (empty_square ?to_file ?to_rank)
                             ;capturable piece at destination:
                             (and
                                 (not(empty_square ?to_file ?to_rank))
                                 (not(occupied_by_same_color ?rook ?to_file ?to_rank)) ;capturable piece = opposite color
                             )
                          )
                          (myturn ?rook)
                          (or 
                              (and
                                  (= ?from_file ?to_file) ;vertical movement
                                  (not(= ?from_rank ?to_rank))
                                  (vert_reachable ?from_file ?from_rank ?to_file ?to_rank)
                              )
                              (and
                                  (= ?from_rank ?to_rank) ;horizontal movement
                                  (not(= ?from_file ?to_file))
                                  (horiz_reachable ?from_file ?from_rank ?to_file ?to_rank)
                              )
                          )
                          (not(my_king_in_check ?rook))
                      )
        :effect (and 
                     (not(at ?rook ?from_file ?from_rank))
                     (forall (?figure - figure)
                        (when 
                            (and
                                (at ?figure ?to_file ?to_rank)
                                (not(same_color ?rook ?figure)) ;capturable piece = opposite color
                            )
                            (and
                                (not (at ?figure ?to_file ?to_rank))
                                (removed ?figure)
                                ;(not(is_on_board ?figure))
                            )
                        )
                     )
                     (at ?rook ?to_file ?to_rank)
                     (not(not_moved ?rook))
                     (when (white_s_turn)
                        (not(white_s_turn))
                     )
                     (when (not(white_s_turn))
                        (white_s_turn)
                     )
                     (empty_square ?from_file ?from_rank)
                     (not(empty_square ?to_file ?to_rank))
                     (last_piece_moved ?rook)
                )
    )
    (:action queen_move
        :parameters (?queen - queen ?from_file ?from_rank ?to_file ?to_rank - location)
        :precondition (and;(is_queen ?queen)
                          (at ?queen ?from_file ?from_rank)
                          ;(is_on_board ?queen)
                          (or ;no piece at destination:
                             (empty_square ?to_file ?to_rank)
                             ;capturable piece at destination:
                             (and
                                 (not(empty_square ?to_file ?to_rank))
                                 (not(occupied_by_same_color ?queen ?to_file ?to_rank)) ;capturable piece = opposite color
                             )
                          )
                          (myturn ?queen)
                          (or 
                              (and
                                  (= ?from_file ?to_file) ;vertical movement
                                  (not(= ?from_rank ?to_rank))
                                  (vert_reachable ?from_file ?from_rank ?to_file ?to_rank)
                              )
                              (and
                                  (= ?from_rank ?to_rank) ;horizontal movement
                                  (not(= ?from_file ?to_file))
                                  (horiz_reachable ?from_file ?from_rank ?to_file ?to_rank)
                              )
                              (and
                                  (not(= ?from_rank ?to_rank)) ;diagonal movement
                                  (not(= ?from_file ?to_file))
                                  (diag_reachable ?from_file ?from_rank ?to_file ?to_rank)
                              )
                          )
                          (not(my_king_in_check ?queen))
                      )
        :effect (and 
                     (not (at ?queen ?from_file ?from_rank))
                     (forall (?figure - figure)
                        (when 
                            (and
                                (at ?figure ?to_file ?to_rank)
                                (not(same_color ?queen ?figure)) ;capturable piece = opposite color
                            )
                            (and
                                (not (at ?figure ?to_file ?to_rank))
                                (removed ?figure)  
                                ;(not(is_on_board ?figure))
                            )
                        )
                     )
                     (at ?queen ?to_file ?to_rank)
                     (when (white_s_turn)
                        (not(white_s_turn))
                     )
                     (when (not(white_s_turn))
                        (white_s_turn)
                     )
                     (empty_square ?from_file ?from_rank)
                     (not(empty_square ?to_file ?to_rank))
                     (last_piece_moved ?queen)
                )
    )
    (:action king_move 
        :parameters (?king - king ?from_file ?from_rank ?to_file ?to_rank - location)
        :precondition (and ;(is_king ?king)
                           (at ?king ?from_file ?from_rank)  
                           ;(is_on_board ?king)                       
                           (myturn ?king)
                           ;(not(and(= ?from_file ?to_file)
                           ;        (= ?from_rank ?to_rank)
                           ;))
                           ;(or(diff_by_One ?from_file ?to_file)
                           ;   (diff_by_One ?from_rank ?to_rank)
                           ;)
                           ;(not
                           ;     (and
                           ;         (not(= ?from_file ?to_file))
                           ;         (not(= ?from_rank ?to_rank))
                           ;         (not(diff_by_One ?from_file ?to_file))
                           ;         (not(diff_by_One ?from_rank ?to_rank))
                           ;     )
                           ;)
                           (or
                               (and ;diagonal move
                                   (diff_by_One ?from_file ?to_file)
                                   (diff_by_One ?from_rank ?to_rank)
                               )
                               (and ;vertical move
                                   (= ?from_rank ?to_rank)
                                   (diff_by_One ?from_file ?to_file)
                               )
                               (and ;horizontal move
                                   (= ?from_file ?to_file)
                                   (diff_by_One ?from_rank ?to_rank)
                               )
                           )
                           (not(red_zone ?king ?to_file ?to_rank))
                      )
        :effect (and 
                     (not (at ?king ?from_file ?from_rank))
                     (at ?king ?to_file ?to_rank)
                     (forall (?figure - figure)
                        (when 
                            (and
                                (at ?figure ?to_file ?to_rank)
                                (not(same_color ?king ?figure)) ;capturable piece = opposite color
                            )
                            (and
                                (not (at ?figure ?to_file ?to_rank))
                                (removed ?figure)
                                ;(not(is_on_board ?figure))
                            )
                        )
                     )
                     (not(not_moved ?king)) ;important for castling only
                     (when (white_s_turn)
                        (not(white_s_turn))
                     )
                     (when (not(white_s_turn))
                        (white_s_turn)
                     )
                     (empty_square ?from_file ?from_rank) ;the from square is now empty
                     (not(empty_square ?to_file ?to_rank)) ;the to square is not empty
                     (last_piece_moved ?king)
                )
    )
    (:action castling
        ;TODO:TEST THIS
        ;TODO: Only works with FEN CODE if we have 2 rooks on the board because the rooks get classified wrongly in start and goal pos
        :parameters (?king - king ?rook - rook ?from_file_king ?from_file_rook ?to_file_king ?rank1 ?to_file_rook ?rank2 - location)
        :precondition (and 
                           (not_moved ?king)
                           (not_moved ?rook)
                           ;(is_on_board ?king)
                           ;(is_on_board ?rook)
                           (myturn ?king)
                           (myturn ?rook)
                           (same_color ?rook ?king)
                           (= ?rank1 ?rank2) ;stay the same, they are just here so my output prints a readable plan
                           (diff_by_Two ?from_file_king ?to_file_king)
                           (at ?king ?from_file_king ?rank1)
                           (at ?rook ?from_file_rook ?rank1)
                           (or 
                               (and(kingside_rook ?rook);kingside castling
                                   (diff_by_Two ?from_file_rook ?to_file_rook)
                                   (plusOne ?to_file_rook ?to_file_king) ;king on the right of the rook
                               )
                               (and
                                   (queenside_rook ?rook);queenside castling
                                   (diff_by_Three ?from_file_rook ?to_file_rook)
                                   (minusOne ?to_file_rook ?to_file_king) ;king on the left of the rook
                               )
                           )
                           (king_to_rook_possible ?rook ?rank1 ?from_file_king ?from_file_rook) ;to check if any figure between rook and king
                           ;(not(move_through_red_zone ?king ?from_file_king ?rank1 ?to_file_king))
                           ;(red_zone ?from_file_king ?rank1) ;TODO: king cannot be in check in start position
                      )
        :effect (and 
                     (at ?king ?to_file_king ?rank1)
                     (at ?rook ?to_file_rook ?rank1)
                     (not(not_moved ?king))
                     (not(not_moved ?rook))
                     (not(at ?king ?from_file_king ?rank1))
                     (not(at ?rook ?from_file_rook ?rank1))
                     (when (white_s_turn)
                        (not(white_s_turn))
                     )
                     (when (not(white_s_turn))
                        (white_s_turn)
                     )
                     (empty_square ?from_file_king ?rank1)
                     (empty_square ?from_file_rook ?rank1)
                     (not(empty_square ?to_file_king ?rank1))
                     (not(empty_square ?to_file_rook ?rank1))
                )
    )    
)