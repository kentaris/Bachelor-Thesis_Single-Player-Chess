(define (domain chess)
    (:requirements :negative-preconditions :strips :typing)
    (:types
        location pawn_b knight_b w_bishop_b b_bishop_b rook_b queen_b king_b PAWN_w KNIGHT_w W_BISHOP_w B_BISHOP_w ROOK_w QUEEN_w KING_w - object
        black white - pawn_b
        black white - knight_b
        black white - w_bishop_b
        black white - b_bishop_b
        black white - rook_b
        black white - queen_b
        black white - king_b
        black white - PAWN_w
        black white - KNIGHT_w
        black white - W_BISHOP_w
        black white - B_BISHOP_w
        black white - ROOK_w
        black white - QUEEN_w
        black white - KING_w
    )
    (:predicates
        (at ?figure - figure ?square - object)
        (visited ?square - object)
        (valid_move ?square_from ?square_to - object)
        (empty ?square_to - object)
        (whites_turn)
        (blacks_turn)
    )

    ;Taking turns:
    (:action take_turn
     :precondition (blacks_turn)
     :effect (and(whites_turn)
                 (not(blacks_turn))
             )
    )
    (:action take_turn
     :precondition (whites_turn)
     :effect (and(blacks_turn)
                 (not(whites_turn))
             )
    )

    (:action move
        :parameters (?from ?to)
        :precondition (and (at ?from)
                           (valid_move ?from ?to)
                           (not (visited ?to)))
        :effect (and (not (at ?from))
                    (at ?to)
                    (visited ?to))
    )
)