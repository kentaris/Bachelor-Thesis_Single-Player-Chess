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
        (is_knight ?knight - knight)
        (is_bishop ?bishop - bishop)
        (is_rook ?rook - rook)
        (is_queen ?queen - queen)
        (is_king ?king - king)
        (TRUE)
        (FALSE)
     ;fluent/normal predicates:
        (at ?figure - figure ?file ?rank - location)
        (not_moved ?figure - figure)
        (removed ?figure - figure)
        (white_s_turn)
        (double_moved ?pawn - pawn)

     ;derived predicates:
        (occupied ?file ?rank - location)
        (occupied_by_same_color ?figure - figure ?file ?rank - location)
        (occupied_by_figure ?figure - figure ?file ?rank - location)

        (horiz_adj ?from_file ?from_rank ?to_file ?to_rank - location)
        (vert_adj ?from_file ?from_rank ?to_file ?to_rank - location)
        (diag_adj ?from_file ?from_rank ?to_file ?to_rank - location)
        (same_diag ?from_file ?from_rank ?next_file ?next_rank ?to_file ?to_rank - location)
        (between ?from ?next ?to - location)

        (vert_reachable ?from_file ?from_rank ?to_file ?to_rank - location)
        (horiz_reachable ?from_file ?from_rank ?to_file ?to_rank - location)
        (vert_reachable2 ?from_file ?from_rank ?to_file ?to_rank - location)
        (horiz_reachable2 ?from_file ?from_rank ?to_file ?to_rank - location)
        (diag_reachable ?from_file ?from_rank ?to_file ?to_rank - location)

        (king_to_rook_possible ?rook - rook ?rank ?from_file_king ?to_file_king - location)
        (kingside_rook ?rook - rook)
        (queenside_rook ?rook - rook)

        (red_zone ?figure - figure ?to_file ?to_rank - location)
    )
;DERIVED PREDICATES:
 ;;;;;;;;;;;;;;;;;;;;
 ;Square info:
    (:derived (occupied ?file ?rank - location) ;check if some figure is at location
        (exists(?figure - figure)
            (at ?figure ?file ?rank)
        )
    )
    (:derived (occupied_by_figure ?figure - figure ?file ?rank - location) ;check if some figure is at location with same color
        (at ?figure ?file ?rank)
    )
    (:derived (occupied_by_same_color ?figure - figure ?file ?rank - location) ;check if some figure is at location with same color
        (and
            (occupied ?file ?rank)
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
                    (plusOne ?next_file ?to_file)
                    (plusOne ?from_rank ?next_rank)
                    (plusOne ?next_rank ?to_rank)
                );rank-1/file-1
                (and(minusOne ?from_file ?next_file)
                    (minusOne ?next_file ?to_file)
                    (minusOne ?from_rank ?next_rank)
                    (minusOne ?next_rank ?to_rank)
                );rank-1/file+1
                (and(plusOne ?from_file ?next_file)
                    (plusOne ?next_file ?to_file)
                    (minusOne ?from_rank ?next_rank)
                    (minusOne ?next_rank ?to_rank)
                );rank+1/file-1
                (and(minusOne ?from_file ?next_file)
                    (minusOne ?next_file ?to_file)
                    (plusOne ?from_rank ?next_rank)
                    (plusOne ?next_rank ?to_rank)
                )
            )
        )
    )
    (:derived (between ?from ?next ?to - location) ;TODO: test if i need to extend this by plusOne_nTimes & minusOne....
        (and
            (not(= ?from ?next))
            (not(= ?next ?to))
            (or
                (and
                    (plusOne ?from ?next)
                    (plusOne_nTimes ?next ?to) ;TODO: test : plusOne_nTimes
                )
                (and
                    (minusOne ?from ?next)
                    (minusOne_nTimes ?next ?to) ;TODO: test : minusOne_nTimes
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
    (:derived (vert_reachable2 ?from_file ?from_rank ?to_file ?to_rank - location)
        (or (and
                (vert_adj ?from_file ?from_rank ?to_file ?to_rank)
            )
            (exists(?next_rank - location)
                 (and 
                      (not(= ?from_rank ?next_rank)) ;don't stay on same rank
                      (diff_by_One ?from_rank ?next_rank) ;one step
                      (= ?from_file ?to_file) ;same file
                      (between ?from_rank ?next_rank ?to_rank)
                      (vert_reachable2 ?from_file ?next_rank ?to_file ?to_rank)
                 )
            )
        )
    )
    (:derived (horiz_reachable2 ?from_file ?from_rank ?to_file ?to_rank - location)
        (or (and
                (horiz_adj ?from_file ?from_rank ?to_file ?to_rank)
            )
            (exists(?next_file - location)
                 (and 
                      (not(= ?from_file ?to_file)) ;don't stay on same file
                      (diff_by_One ?from_file ?next_file) ;one step at a time
                      (= ?from_rank ?to_rank) ;same rank
                      (between ?from_file ?next_file ?to_file)
                      (horiz_reachable2 ?next_file ?from_rank ?to_file ?to_rank)
                 )
            )
        )
    )
    (:derived (vert_reachable ?from_file ?from_rank ?to_file ?to_rank - location)
        (or (and(or(not(occupied ?to_file ?to_rank))
                   (and (occupied ?to_file ?to_rank)
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
                 (and (not(occupied ?to_file ?next_rank))
                      (not(= ?from_rank ?next_rank)) ;don't stay on same rank
                      (diff_by_One ?from_rank ?next_rank) ;one step
                      (= ?from_file ?to_file) ;same file
                      (between ?from_rank ?next_rank ?to_rank)
                      (vert_reachable ?from_file ?next_rank ?to_file ?to_rank)
                 )
            )
        )
    )
    (:derived (horiz_reachable ?from_file ?from_rank ?to_file ?to_rank - location)
        (or (and(or(not(occupied ?to_file ?to_rank))
                   (and (occupied ?to_file ?to_rank)
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
                 (and (not(occupied ?next_file ?to_rank))
                      (not(= ?from_file ?to_file)) ;don't stay on same file
                      (diff_by_One ?from_file ?next_file) ;one step at a time
                      (= ?from_rank ?to_rank) ;same rank
                      (between ?from_file ?next_file ?to_file)
                      (horiz_reachable ?next_file ?from_rank ?to_file ?to_rank)
                 )
            )
        )
    )
    (:derived (diag_reachable ?from_file ?from_rank ?to_file ?to_rank - location)
        (or (and(or(not(occupied ?to_file ?to_rank))
                   (and (occupied ?to_file ?to_rank)
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
                        (not(= ?from_file ?next_file)) ;don't stay on same file
                        (not(= ?from_rank ?next_rank)) ;don't stay on same rank
                        (diff_by_One ?from_file ?next_file) ;one step at a time
                        (diff_by_One ?from_rank ?next_rank) ;one step at a time
                        (not(occupied ?next_file ?next_rank)) ;TODO: capturable?
                        (same_diag ?from_file ?from_rank ?next_file ?next_rank ?to_file ?to_rank) ;bishop needs to stay on the same diagonal
                        (diag_reachable ?next_file ?next_rank ?to_file ?to_rank)
                   )
            )
        )
    )
    
 ;king predicates:
    (:derived (red_zone ?king - king ?kt_file ?kt_rank - location) ;for some figure (capturer) on the board: is there a king of opposite color it can move to?
        (or
            (exists(?pawn - pawn ?c_file ?c_rank - location)
                (and(at ?pawn ?c_file ?c_rank)
                    (diag_adj ?c_file ?c_rank ?kt_file ?kt_rank)
                    (diff_by_One ?kt_file ?c_file) ;diagonal capure
                    (not(= ?kt_file ?c_file))
                    (not(= ?kt_rank ?c_rank))
                    (and(or (and(is_black ?pawn)
                                (is_white ?king)
                                (minusOne ?c_rank ?kt_rank)
                            )
                            (and(is_white ?pawn)
                                (is_black ?king)
                                (plusOne ?c_rank ?kt_rank)
                            )
                        )
                    )              
                )
            )
            (exists(?knight - knight ?c_file ?c_rank - location)
                (and(at ?knight ?c_file ?c_rank)
                    (not(= ?c_file ?kt_file)) ;kt_file & kt_rank should = landing position for the knight
                    (not(= ?c_rank ?kt_rank))
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
            ;(exists(?bishop - bishop ?c_file ?c_rank - location)
            ;    (and(at ?bishop ?c_file ?c_rank)
            ;        (FALSE)
            ;    )
            ;)
            (exists(?rook - rook ?c_file ?c_rank - location)
                (and(at ?rook ?c_file ?c_rank) ;valid starting location
                    ;TODO: optimize by checking if it's on the same horiz, vertic- line               
                    ;(not(at ?rook ?kt_file ?kt_rank)) ;rook is not at king position
                    ;(not(at ?king ?c_file ?c_rank)) ;king is not at rook position

                    ;king cannot be checked by his own pieces:
                    (and(or (and(is_black ?rook)
                                (is_white ?king)
                            )
                            (and(is_white ?rook)
                                (is_black ?king)
                            )
                        )
                    )
                    ;can the king be reached by the capturer:
                    (or (and(= ?kt_file ?c_file) ;vertical movement
                            (not(= ?kt_rank ?c_rank))
                            (vert_reachable ?c_file ?c_rank ?kt_file ?kt_rank)
                        )
                        (and(= ?kt_rank ?c_rank) ;horizontal movement
                            (not(= ?kt_file ?c_file))
                            (horiz_reachable ?c_file ?c_rank ?kt_file ?kt_rank)
                        )
                    )
                )
            )
        )
    )
;ACTIONS
 ;;;;;;;;
    ;(:action en_passant
    ;    :parameters (?pawn - pawn ?from_file ?from_rank ?to_file ?to_rank - location)
    ;    :precondition (and (double_moved ?pawn) ;TODO: we also need to check if the double move happend in the last turn: forall
    ;                  )
    ;    :effect (
    ;            )
    ;)
    
    (:action pawn_capture ;TODO: check if moving causes own king to be checked
        :parameters (?pawn - pawn ?from_file ?from_rank ?to_file ?to_rank - location)
        :precondition (and
                           ;(is_pawn ?pawn)
                           (at ?pawn ?from_file ?from_rank)
                           (diff_by_One ?from_file ?to_file)
                           (diff_by_One ?from_rank ?to_rank)
                           (not(= ?from_file ?to_file))
                           (not(= ?from_rank ?to_rank))
                           (occupied ?to_file ?to_rank) ;there is a piece on it
                           (not(occupied_by_same_color ?pawn ?to_file ?to_rank)) ;it is not my own color
                           (and ;diagonal capture:
                                (or(and(plusOne ?from_rank ?to_rank) ;white pawns move up the board only
                                       (is_white ?pawn)
                                   )
                                   (and(minusOne ?from_rank ?to_rank) ;black pawns move down the board only
                                       (is_black ?pawn)
                                       (diff_by_One ?from_file ?to_file)
                                   )
                                )
                           )
                      )
        :effect (and (not (at ?pawn ?from_file ?from_rank))
                     (forall (?figure - figure)
                        (when (and(at ?figure ?to_file ?to_rank)
                                (not(occupied_by_same_color ?pawn ?to_file ?to_rank))) ;capturable piece = opposite color
                            (and(not (at ?figure ?to_file ?to_rank))
                                (removed ?figure))
                        )
                     )
                     (at ?pawn ?to_file ?to_rank)
                     (not(white_s_turn))
                )
    )
    (:action pawn_move_one
        :parameters (?pawn - pawn ?from_file ?from_rank ?to_file ?to_rank - location)
        :precondition (and 
                           ;(is_pawn ?pawn)
                           (at ?pawn ?from_file ?from_rank)
                           ;(not(at ?pawn ?to_file ?to_rank))
                           (not(= ?from_rank ?to_rank))
                           (= ?from_file ?to_file)
                           (not(occupied ?to_file ?to_rank))
                           (diff_by_One ?from_rank ?to_rank)
                           (or    
                               (and(plusOne ?from_rank ?to_rank) ;single move white
                                   (is_white ?pawn)
                               )
                               (and(minusOne ?from_rank ?to_rank) ;single move black
                                   (is_black ?pawn)
                               )
                           )
                       )
        :effect (and (not (at ?pawn ?from_file ?from_rank))
                     (at ?pawn ?to_file ?to_rank)
                     (not(white_s_turn))
                )
    )
    (:action pawn_move_two
        :parameters (?pawn - pawn ?from_file ?from_rank ?to_file ?to_rank - location)
        :precondition (and (not(double_moved ?pawn))
                           ;(is_pawn ?pawn)
                           (at ?pawn ?from_file ?from_rank)
                           ;(not(at ?pawn ?to_file ?to_rank))
                           (not(= ?from_rank ?to_rank))
                           (= ?from_file ?to_file)
                           (not(occupied ?to_file ?to_rank))
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
                       )
        :effect (and (not (at ?pawn ?from_file ?from_rank))
                     (at ?pawn ?to_file ?to_rank)
                     (not(white_s_turn))
                     (not(double_moved ?pawn))
                )
    )
    (:action knight_move
        :parameters (?knight - knight ?from_file ?from_rank ?to_file ?to_rank - location)
        :precondition (and ;(is_knight ?knight)
                           (at ?knight ?from_file ?from_rank)
                           (not(= ?from_file ?to_file))
                           (not(= ?from_rank ?to_rank))
                           (or
                               (and ;two files, one row:
                                   (diff_by_Two ?from_file ?to_file) ; file +/- 2
                                   (diff_by_One ?from_rank ?to_rank)) ; rank +/- 1
                               (and ;two rows, one file:
                                   (diff_by_Two ?from_rank ?to_rank) ; rank +/- 2
                                   (diff_by_One ?from_file ?to_file) ; file +/- 1
                               )
                           )
                           (or(not(occupied ?to_file ?to_rank))
                              (not(occupied_by_same_color ?knight ?to_file ?to_rank)) ;capturable piece = opposite color
                           )
                       )
        :effect (and (not (at ?knight ?from_file ?from_rank))
                     (forall (?figure - figure)
                        (when (and(at ?figure ?to_file ?to_rank)
                                  (not(occupied_by_same_color ?knight ?to_file ?to_rank)) ;capturable piece = opposite color
                              )
                            (and(not (at ?figure ?to_file ?to_rank))
                                (removed ?figure))
                        )
                     )
                     (at ?knight ?to_file ?to_rank)
                     (not(white_s_turn))
                )
    )    
    (:action bishop_move
        :parameters (?bishop - bishop ?from_file ?from_rank ?to_file ?to_rank - location)
        :precondition (and;(is_bishop ?bishop)
                          (at ?bishop ?from_file ?from_rank)
                          ;(not(at ?bishop ?to_file ?to_rank))
                          (not(= ?from_file ?to_file))
                          (not(= ?from_rank ?to_rank))
                          (or ;no piece at destination:
                             (not(occupied ?to_file ?to_rank))
                             ;capturable piece at destination:
                             (and(occupied ?to_file ?to_rank)
                                 (not(occupied_by_same_color ?bishop ?to_file ?to_rank)) ;capturable piece = opposite color
                             )
                          )
                          (diag_reachable ?from_file ?from_rank ?to_file ?to_rank)
                      )
        :effect (and (not (at ?bishop ?from_file ?from_rank))
                     (forall (?figure - figure)
                        (when (and(at ?figure ?to_file ?to_rank)
                                  (not(occupied_by_same_color ?bishop ?to_file ?to_rank)) ;capturable piece = opposite color
                              )
                            (and(not (at ?figure ?to_file ?to_rank))
                                (removed ?figure))
                        )
                     )
                     (at ?bishop ?to_file ?to_rank)
                     (not(white_s_turn))
                )
    )
    (:action rook_move
        :parameters (?rook - rook ?from_file ?from_rank ?to_file ?to_rank - location)
        :precondition (and;(is_rook ?rook)
                          (at ?rook ?from_file ?from_rank)
                          ;(not(at ?rook ?to_file ?to_rank))
                          (or ;no piece at destination:
                             (not(occupied ?to_file ?to_rank))
                             ;capturable piece at destination:
                             (and(occupied ?to_file ?to_rank)
                                 (not(occupied_by_same_color ?rook ?to_file ?to_rank)) ;capturable piece = opposite color
                             )
                          )
                          (or (and(= ?from_file ?to_file) ;vertical movement
                                  (not(= ?from_rank ?to_rank))
                                  ;(vert_reachable ?from_file ?from_rank ?to_file ?to_rank)
                              )
                              (and(= ?from_rank ?to_rank) ;horizontal movement
                                  (not(= ?from_file ?to_file))
                                  ;(horiz_reachable ?from_file ?from_rank ?to_file ?to_rank)
                              )
                          )
                      )
        :effect (and (not (at ?rook ?from_file ?from_rank))
                     (forall (?figure - figure)
                        (when (and(at ?figure ?to_file ?to_rank)
                                  (not(occupied_by_same_color ?rook ?to_file ?to_rank)) ;capturable piece = opposite color
                              )
                            (and(not (at ?figure ?to_file ?to_rank))
                                (removed ?figure))
                        )
                     )
                     (at ?rook ?to_file ?to_rank)
                     (not(not_moved ?rook))
                     (not(white_s_turn))
                )
    )
    (:action queen_move
        :parameters (?queen - queen ?from_file ?from_rank ?to_file ?to_rank - location)
        :precondition (and;(is_queen ?queen)
                          (at ?queen ?from_file ?from_rank)
                          ;(not(at ?queen ?to_file ?to_rank))
                          (or ;no piece at destination:
                             (not(occupied ?to_file ?to_rank))
                             ;capturable piece at destination:
                             (and(occupied ?to_file ?to_rank)
                                 (not(occupied_by_same_color ?queen ?to_file ?to_rank)) ;capturable piece = opposite color
                             )
                          )
                          (or (and(= ?from_file ?to_file) ;vertical movement
                                  (not(= ?from_rank ?to_rank))
                                  (vert_reachable ?from_file ?from_rank ?to_file ?to_rank)
                              )
                              (and(= ?from_rank ?to_rank) ;horizontal movement
                                  (not(= ?from_file ?to_file))
                                  (horiz_reachable ?from_file ?from_rank ?to_file ?to_rank)
                              )
                              (and(not(= ?from_rank ?to_rank)) ;diagonal movement
                                  (not(= ?from_file ?to_file))
                                  (diag_reachable ?from_file ?from_rank ?to_file ?to_rank)
                              )
                          )
                      )
        :effect (and (not (at ?queen ?from_file ?from_rank))
                     (forall (?figure - figure)
                        (when (and(at ?figure ?to_file ?to_rank)
                                  (not(occupied_by_same_color ?queen ?to_file ?to_rank)) ;capturable piece = opposite color
                              )
                            (and(not (at ?figure ?to_file ?to_rank))
                                (removed ?figure))
                        )
                     )
                     (at ?queen ?to_file ?to_rank)
                     (not(white_s_turn))
                )
    )
    (:action king_move 
        :parameters (?king - king ?from_file ?from_rank ?to_file ?to_rank - location)
        :precondition (and ;(is_king ?king)
                           (at ?king ?from_file ?from_rank)                           
                           ;(not(at ?king ?to_file ?to_rank))
                           ;(occupied_by_figure ?king ?from_file ?from_rank)
                           ;;TODO: test if this works in all szenarios: 
                           ;(not(red_zone ?king ?to_file ?to_rank))
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
                      )
        :effect (and (not (at ?king ?from_file ?from_rank))
                     (forall (?figure - figure)
                        (when (and(at ?figure ?to_file ?to_rank)
                                  (not(occupied_by_same_color ?king ?to_file ?to_rank)) ;capturable piece = opposite color
                              )
                            (and(not (at ?figure ?to_file ?to_rank))
                                (removed ?figure))
                        )
                     )
                     (at ?king ?to_file ?to_rank)
                     (not(not_moved ?king))
                     (not(white_s_turn))
                )
    )
    ;(:action castling ;TODO: can't castle into check
    ;    ;TODO: Only works with FEN CODE if we have 2 rooks on the board because the rooks get classified wrongly in start and goal pos
    ;    :parameters (?king - king ?rook - rook ?from_file_king ?from_file_rook ?to_file_king ?rank1 ?to_file_rook ?rank2 - location)
    ;    :precondition (and (not_moved ?king)
    ;                       (not_moved ?rook)
    ;                       (= ?rank1 ?rank2) ;stay the same, they are just here so my output prints a readable plan
    ;                       (diff_by_Two ?from_file_king ?to_file_king)
    ;                       (at ?king ?from_file_king ?rank1)
    ;                       (at ?rook ?from_file_rook ?rank1)
    ;                       (not(at ?king ?to_file_king ?rank1))
    ;                       (not(at ?rook ?to_file_rook ?rank1))
    ;                       (or (and(kingside_rook ?rook);kingside castling
    ;                               (diff_by_Two ?from_file_rook ?to_file_rook)
    ;                               (plusOne ?to_file_rook ?to_file_king) ;king on the right of the rook
    ;                           )
    ;                           (and(queenside_rook ?rook);queenside castling
    ;                               (diff_by_Three ?from_file_rook ?to_file_rook)
    ;                               (minusOne ?to_file_rook ?to_file_king) ;king on the left of the rook
    ;                           )
    ;                       )
    ;                       (occupied_by_figure ?rook ?from_file_rook ?rank1)
    ;                       ;(occupied_by_same_color ?rook ?from_file_king ?rank1) ;doesn't work somehow.... I don't see why
    ;                       (king_to_rook_possible ?rook ?rank1 ?from_file_king ?from_file_rook) ;to check if any figure between rook and king
    ;                  )
    ;    :effect (and (at ?king ?to_file_king ?rank1)
    ;                 (at ?rook ?to_file_rook ?rank1)
    ;                 (not(not_moved ?king))
    ;                 (not(not_moved ?rook))
    ;                 (not(at ?king ?from_file_king ?rank1))
    ;                 (not(at ?rook ?from_file_rook ?rank1))
    ;                 (not(white_s_turn))
    ;            )
    ;)
)