(define (problem chess-problem)
    (:domain chess)
    (:objects
        ;locations:
;[:locations]
        
        ;object pieces:
;[:object_pieces]
    )
    (:init
        (valid_position)
        ;initial state s_0:
;[:init_start_state]
;[:init_pawn_start_pos]
;[:init_plusOne]
;[:init_diffByN]
;[:init_diffBy]
;[:init_Same_color]
            
        ;figures on the board:
;[:is_on_board]
        
        ;last pawn line:
;[:last_pawn_line]
        
        ;castling:
;[:castling]
        
        ;colors:
;[:colors]
        
        ;piece types:
;[:piece_types]
        
        ;turn:
;[:whos_turn]

;[:pawn_promotion_extra_pieces]
        
;[:adjacent]
;[:same_diag]
;[:between]

        (TRUE)
    )
    (:goal (and
                (valid_position)
        ;goal state s_*:
            ;board:
;[:goal_position]
            ;removed pieces:
;[:removed]
           )
    )
)