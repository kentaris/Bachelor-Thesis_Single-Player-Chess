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
        (diff_by_Zero ?file ?rank - location)
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

     ;derived predicates:
        (empty ?file ?rank - location)
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
        (diag_reachable ?from_file ?from_rank ?to_file ?to_rank - location)

        (capturable ?figure - figure ?to_file ?to_rank - location)

        (king_to_rook_possible ?rook - rook ?rank ?from_file_king ?to_file_king - location)
        (kingside_rook ?rook - rook)
        (queenside_rook ?rook - rook)
        (king_move_into_check ?king - king ?to_file ?to_rank - location)
        (red_zone ?figure - figure ?to_file ?to_rank - location)
        (king_capturable_by_rook ?rook - rook ?from_file ?from_rank ?to_file ?to_rank - location)
    )
;DERIVED PREDICATES:
 ;;;;;;;;;;;;;;;;;;;;
 ;Square info:
    (:derived (occupied ?file ?rank - location) ;check if some figure is at location
        (exists(?figure - figure)
            (at ?figure ?file ?rank)
        )
    )
    (:derived (empty ?file ?rank - location) ;check location is empty
        (not(exists(?figure - figure)
            (at ?figure ?file ?rank)
        ))
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
                    (plusOne ?next ?to) ;TODO: test : plusOne_nTimes
                )
                (and
                    (minusOne ?from ?next)
                    (minusOne ?next ?to) ;TODO: test : minusOne_nTimes
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
        (or (and(not(occupied ?to_file ?to_rank))
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
        (or (and(not(occupied ?to_file ?to_rank))
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
        (or (and(not(occupied ?to_file ?to_rank))
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
 ;capturable
    (:derived (capturable ?figure - figure ?to_file ?to_rank - location)
        (not(occupied_by_same_color ?figure ?to_file ?to_rank)) ;it is not my own color
    )
    
 ;king predicates:
    ;;TODO: check if King is walking through a check by castling or if king is in check when castling
    ;(:derived (king_to_rook_possible ?rook - rook ?rank ?from_file_king ?from_file_rook - location) ;checks if the rook can move up to the king without there being any pieces inbetween
    ;    (or (and(occupied_by_figure ?rook ?from_file_rook ?rank)
    ;            ;(occupied_by_same_color ?rook ?from_file_king ?rank) ;check if king has same color as rook... oesn't work because ?from_file_king gets replaceswith ?next_file_king and there will never be a king at it so recursion doesn't work
    ;            (horiz_adj ?from_file_king ?rank ?from_file_rook ?rank)
    ;        )
    ;        (exists (?next_file_king - location) 
    ;            (and(diff_by_One ?from_file_king ?next_file_king)
    ;                (or(not(occupied ?next_file_king ?rank))
    ;                   (and(occupied_by_figure ?rook ?next_file_king ?rank)
    ;                       (occupied_by_same_color ?rook ?next_file_king ?rank)
    ;                   )
    ;                )
	;		        (king_to_rook_possible ?rook ?rank ?next_file_king ?from_file_rook)
    ;            )
    ;        )
    ;    )
    ;)

    (:derived (red_zone ?figure - figure ?kt_file ?kt_rank - location) ;for some figure (capturer) on the board: is there a king of opposite color it can move to?
        (exists(?rook - rook ?cf_file ?cf_rank - location)
            ;TODO: check if two of the same pieces can be messed up/exchanged
            (and(at ?rook ?cf_file ?cf_rank) ;valid starting location
                ;TODO: optimize by checking if it's on the same diagonal, horiz, vertic- line or if it is a Knight. we can ignore all other pieces                
                (at ?rook ?kt_file ?kt_rank) ;rook is not at king position
                (not(at ?rook ?cf_file ?cf_rank)) ;rook is where he's supposed to be
                (not(occupied_by_same_color ?rook ?cf_file ?cf_rank)) ;king cannot be checked by his own pieces
                ;can the king be reached by the capturer:
                (or (and(= ?kt_file ?cf_file) ;vertical movement
                        (not(= ?kt_rank ?ct_rank))
                        (vert_reachable ?kt_file ?kt_rank ?ct_file ?ct_rank)
                    )
                    (and(= ?kt_rank ?ct_rank) ;horizontal movement
                        (not(= ?kt_file ?ct_file))
                        (horiz_reachable ?kt_file ?kt_rank ?ct_file ?ct_rank)
                    )
                )
            )
        )
    )
    ;the '?from_file' and '?from_rank' variables are the location to which the king wants to move (the should be location). from here we check if he can move to a piece that can capture him (including the king because they can't come too close o thats the same thing).
    ;(:derived (king_move_into_check ?king - king ?from_file ?from_rank - location) ;TODO: include this in piece movements: piece can't move if it's own colored king is checked by moving
    ;    (exists(?to_file ?to_rank - location)
    ;        (and (or (and(= ?from_file ?to_file) ;vertical movement
    ;                     (not(= ?from_rank ?to_rank))
    ;                     ;(vert_reachable_capturer_piece ?king ?from_file ?from_rank ?to_file ?to_rank)
    ;                 )
    ;                 (and(= ?from_rank ?to_rank) ;horizontal movement
    ;                     (not(= ?from_file ?to_file))
    ;                     ;(horiz_reachable_capturer_piece ?king ?from_file ?from_rank ?to_file ?to_rank)
    ;                 )
    ;                 (and(not(= ?from_rank ?to_rank)) ;diagonal movement
    ;                     (not(= ?from_file ?to_file))
    ;                     ;(diag_reachable_capturer_piece ?king ?from_file ?from_rank ?to_file ?to_rank)
    ;                 )
    ;             )
    ;        )
    ;    )
    ;)
;ACTIONS
 ;;;;;;;;
    (:action en_passant ;TODO: check if moving causes own king to be checked
        :parameters (?pawn - pawn ?from_file ?from_rank ?to_file ?to_rank - location)
        :precondition (and
                           (at ?pawn ?from_file ?from_rank)
                           (not(at ?pawn ?to_file ?to_rank))
                           (not(= ?from_file ?to_file))
                           (not(= ?from_rank ?to_rank))
                           (occupied ?to_file ?to_rank) ;there is a piece on it
                           (not(occupied_by_same_color ?pawn ?to_file ?to_rank)) ;it is not my own color
                           (at ?pawn ?from_file ?from_rank)
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
                                (capturable ?pawn ?to_file ?to_rank))
                            (and(not (at ?figure ?to_file ?to_rank))
                                (removed ?figure))
                        )
                     )
                     (at ?pawn ?to_file ?to_rank)
                     (not(white_s_turn))
                )
    )
    (:action pawn_move
        :parameters (?pawn - pawn ?from_file ?from_rank ?to_file ?to_rank - location)
        :precondition (and 
                           (at ?pawn ?from_file ?from_rank)
                           (not(at ?pawn ?to_file ?to_rank))
                           (not(= ?from_rank ?to_rank))
                           (= ?from_file ?to_file)
                           (at ?pawn ?from_file ?from_rank)
                           (not(at ?pawn ?to_file ?to_rank))
                           (not(occupied ?to_file ?to_rank))
                           (or    
                               (or (plusOne ?from_rank ?to_rank) ;single move white
                                   (and ;double move white:
                                       (is_white ?pawn)
                                       (pawn_start_pos_white ?from_file ?from_rank)
                                       (diff_by_Two ?from_rank ?to_rank) ;we don't need to check if it's white or black here because as long as the diff is 2 and it is at starting pos, then the pawn can't go into the wrong direction because it would fall off the board 
                                       (vert_reachable ?from_file ?from_rank ?to_file ?to_rank) 
                                    )
                               )
                               (or (minusOne ?from_rank ?to_rank) ;single move black
                                   (and ;double move black:
                                       (is_black ?pawn)
                                       (pawn_start_pos_black ?from_file ?from_rank)
                                       (diff_by_Two ?from_rank ?to_rank) ;we don't need to check if it's white or black here because as long as the diff is 2 and it is at starting pos, then the pawn can't go into the wrong direction because it would fall off the board 
                                       (vert_reachable ?from_file ?from_rank ?to_file ?to_rank)
                                    )
                               )
                           )
                       )
        :effect (and (not (at ?pawn ?from_file ?from_rank))
                     (at ?pawn ?to_file ?to_rank)
                     (not(white_s_turn))
                )
    )
    (:action knight_move
        :parameters (?knight - knight ?from_file ?from_rank ?to_file ?to_rank - location)
        :precondition (and (at ?knight ?from_file ?from_rank)
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
                              (capturable ?knight ?to_file ?to_rank)
                           )
                       )
        :effect (and (not (at ?knight ?from_file ?from_rank))
                     (forall (?figure - figure)
                        (when (and(at ?figure ?to_file ?to_rank)
                                  (capturable ?knight ?to_file ?to_rank)
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
        :precondition (and
                          (at ?bishop ?from_file ?from_rank)
                          (not(at ?bishop ?to_file ?to_rank))
                          (not(= ?from_file ?to_file))
                          (not(= ?from_rank ?to_rank))
                          (or ;no piece at destination:
                             (not(occupied ?to_file ?to_rank))
                             ;capturable piece at destination:
                             (and(occupied ?to_file ?to_rank)
                                 (capturable ?bishop ?to_file ?to_rank)
                             )
                          )
                          (diag_reachable ?from_file ?from_rank ?to_file ?to_rank)
                      )
        :effect (and (not (at ?bishop ?from_file ?from_rank))
                     (forall (?figure - figure)
                        (when (and(at ?figure ?to_file ?to_rank)
                                  (capturable ?bishop ?to_file ?to_rank)
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
        :precondition (and(at ?rook ?from_file ?from_rank)
                          (not(at ?rook ?to_file ?to_rank))
                          (or ;no piece at destination:
                             (not(occupied ?to_file ?to_rank))
                             ;capturable piece at destination:
                             (and(occupied ?to_file ?to_rank)
                                 (capturable ?rook ?to_file ?to_rank) ;occupied by opposite color
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
                          )
                      )
        :effect (and (not (at ?rook ?from_file ?from_rank))
                     (forall (?figure - figure)
                        (when (and(at ?figure ?to_file ?to_rank)
                                  (capturable ?rook ?to_file ?to_rank)
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
        :precondition (and(at ?queen ?from_file ?from_rank)
                          (not(at ?queen ?to_file ?to_rank))
                          (or ;no piece at destination:
                             (not(occupied ?to_file ?to_rank))
                             ;capturable piece at destination:
                             (and(occupied ?to_file ?to_rank)
                                 (capturable ?queen ?to_file ?to_rank)
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
                                  (capturable ?queen ?to_file ?to_rank)
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
        :precondition (and (at ?king ?from_file ?from_rank)
                           (not(at ?king ?to_file ?to_rank))
                           (or
                               (not(occupied ?to_file ?to_rank))
                               (capturable ?king ?to_file ?to_rank)
                               ;TODO: King can't move into check or capture into check!
                           )
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
        :effect (and (not (at ?king ?from_file ?from_rank))
                     (forall (?figure - figure)
                        (when (and(at ?figure ?to_file ?to_rank)
                                  (capturable ?king ?to_file ?to_rank)
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
    (:action castling ;TODO: can't castle into check
        ;TODO: Only works with FEN CODE if we have 2 rooks on the board because the rooks get classified wrongly in start and goal pos
        :parameters (?king - king ?rook - rook ?from_file_king ?from_file_rook ?to_file_king ?rank1 ?to_file_rook ?rank2 - location)
        :precondition (and (not_moved ?king)
                           (not_moved ?rook)
                           (= ?rank1 ?rank2) ;stay the same, they are just here so my output prints a readable plan
                           (diff_by_Two ?from_file_king ?to_file_king)
                           (at ?king ?from_file_king ?rank1)
                           (at ?rook ?from_file_rook ?rank1)
                           (not(at ?king ?to_file_king ?rank1))
                           (not(at ?rook ?to_file_rook ?rank1))
                           (or (and(kingside_rook ?rook);kingside castling
                                   (diff_by_Two ?from_file_rook ?to_file_rook)
                                   (plusOne ?to_file_rook ?to_file_king) ;king on the right of the rook
                               )
                               (and(queenside_rook ?rook);queenside castling
                                   (diff_by_Three ?from_file_rook ?to_file_rook)
                                   (minusOne ?to_file_rook ?to_file_king) ;king on the left of the rook
                               )
                           )
                           (occupied_by_figure ?rook ?from_file_rook ?rank1)
                           ;(occupied_by_same_color ?rook ?from_file_king ?rank1) ;doesn't work somehow.... I don't see why
                           (king_to_rook_possible ?rook ?rank1 ?from_file_king ?from_file_rook) ;to check if any figure between rook and king
                      )
        :effect (and (at ?king ?to_file_king ?rank1)
                     (at ?rook ?to_file_rook ?rank1)
                     (not(not_moved ?king))
                     (not(not_moved ?rook))
                     (not(at ?king ?from_file_king ?rank1))
                     (not(at ?rook ?from_file_rook ?rank1))
                     (not(white_s_turn))
                )
    )

    ;(:action check_mate
    ;    if the king cannot move anymore and no other action can be taken it is mate
    ;)
    
)