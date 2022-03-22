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
        (at ?figure - figure ?file ?rank - location)
        (not_moved ?figure - figure)
        ;(diff_by_N ?file ?rank - location)
        (diff_by_Zero ?file ?rank - location)
        (diff_by_One ?file ?rank - location)
        (diff_by_Two ?file ?rank - location)
        (diff_by_Three ?file ?rank - location)
        (plusOne ?file ?rank - location)
        (minusOne ?file ?rank - location)
        (pawn_start_pos_white ?from_file ?from_rank - location)
        (pawn_start_pos_black ?from_file ?from_rank - location)
        (is_white ?figure - figure)
        (is_black ?figure - figure)
        (TRUE)
        (FALSE)

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
        (vert_capturable ?from_file ?from_rank ?to_file ?to_rank - location)
        (horiz_capturable ?from_file ?from_rank ?to_file ?to_rank - location)
        (diag_capturable ?from_file ?from_rank ?to_file ?to_rank - location)

        (king_to_rook ?rook - rook ?rank ?from_file_king ?to_file_king - location)
        (kingside_rook ?rook - rook)
        (queenside_rook ?rook - rook)
        (am_I_pinned ?figure - figure ?from_file ?from_rank - location)
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
    (:derived (between ?from ?next ?to - location)
        (and
            (not(= ?from ?next))
            (not(= ?next ?to))
            (or
                (and
                    (plusOne ?from ?next)
                    (plusOne ?next ?to)
                )
                (and
                    (minusOne ?from ?next)
                    (minusOne ?next ?to)
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
    ;TODO: check if King is walking through a check by castling or if king is in check when castling
    (:derived (king_to_rook ?rook - rook ?rank ?from_file_king ?to_file_king - location) ;checks if the rook can move up to the king without there being any pieces inbetween
        (or (and(horiz_adj ?from_file_king ?rank ?to_file_king ?rank)
                (occupied_by_figure ?rook ?to_file_king ?rank)
                ;(occupied_by_same_color ?rook ?to_file_king ?rank) ;if in-check is implemented, this is unnecessary...
            )
            (exists (?next_file_king - location) 
                (and(diff_by_One ?from_file_king ?next_file_king) ;one step at a time
                    (or(not(occupied ?next_file_king ?rank))
                       (occupied_by_figure ?rook ?next_file_king ?rank)
                    )
			        (king_to_rook ?rook ?rank ?next_file_king ?to_file_king)
                )
            )
        )
    )

    ;(:derived (am_I_pinned ?figure - figure ?from_file ?from_rank - location) ;TODO: include this in piece movements: piece can't move if it's own colored king is checked by moving
    ;    (exists (?king_file ?king_rank - location ?king - king)
    ;          (and(at ?king ?king_file ?king_rank)
    ;              (occupied_by_same_color ?figure ?king_file ?king_rank) ;do I have the same color as that king
    ;              (exists (?capturer - figure ?file ?rank - location) ;is there a piece at some position that can capture that king if I am not at my current position
    ;                    (FALSE)
    ;              )
    ;          )
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
                        (when (at ?figure ?to_file ?to_rank)
                            (not (at ?figure ?to_file ?to_rank))
                        )
                     )
                     (at ?pawn ?to_file ?to_rank)
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
                            (not (at ?figure ?to_file ?to_rank))
                        )
                     )
                     (at ?knight ?to_file ?to_rank)
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
                            (not (at ?figure ?to_file ?to_rank))
                        )
                     )
                     (at ?bishop ?to_file ?to_rank)
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
                            (not (at ?figure ?to_file ?to_rank))
                        )
                     )
                     (at ?rook ?to_file ?to_rank)
                     (not(not_moved ?rook))
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
                            (not (at ?figure ?to_file ?to_rank))
                        )
                     )
                     (at ?queen ?to_file ?to_rank)
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
                      )
        :effect (and (not (at ?king ?from_file ?from_rank))
                     (forall (?figure - figure)
                        (when (and(at ?figure ?to_file ?to_rank)
                                  (capturable ?king ?to_file ?to_rank)
                              )
                            (not (at ?figure ?to_file ?to_rank))
                        )
                     )
                     (at ?king ?to_file ?to_rank)
                     (not(not_moved ?king))
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
                           );TODO: check if this is correctly working:
                           (king_to_rook ?rook ?rank1 ?from_file_rook ?to_file_rook) ;to check if any figure between rook and king
                      )
        :effect (and (at ?king ?to_file_king ?rank1)
                     (at ?rook ?to_file_rook ?rank1)
                     (not(not_moved ?king))
                     (not(not_moved ?rook))
                     (not(at ?king ?from_file_king ?rank1))
                     (not(at ?rook ?from_file_rook ?rank1))
                )
    )
)