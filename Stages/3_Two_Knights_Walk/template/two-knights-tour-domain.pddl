;credit: As defined in the book 'An Introduction to the Planning Domain Deﬁnition Language' (Patrik Haslum)
(define (domain knights-tour)
    (:requirements :typing :negative-preconditions :equality)
    (:types
        location - object
        figure - object
    )
    (:predicates
        (at ?knight - figure ?col ?row - location)
        (visited ?col ?row - location)
        (diff_by_one ?col ?row - location)
        (diff_by_two ?col ?row - location)
    )

    (:action move_2col_1row
        :parameters (?knight1 ?knight2 - figure ?from_col ?from_row ?to_col ?to_row - location)
        :precondition (and (at ?knight1 ?from_col ?from_row)
                           (diff_by_two ?from_col ?to_col) ; col +/- 2
                           (diff_by_one ?from_row ?to_row) ; row +/- 1
                           (not (visited ?to_col ?to_row))
                           (not (at ?knight2 ?to_col ?to_row))
                           (not (at ?knight1 ?to_col ?to_row))
                           (not (= ?knight1 ?knight2))
                      )
        :effect (and (not (at ?knight1 ?from_col ?from_row))
                     (at ?knight1 ?to_col ?to_row)
                     (visited ?to_col ?to_row))
    )
    (:action move_2row_1col
        :parameters (?knight1 ?knight2 - figure ?from_col ?from_row ?to_col ?to_row - location)
        :precondition (and (at ?knight1 ?from_col ?from_row)
                           (diff_by_two ?from_row ?to_row) ; row +/- 2
                           (diff_by_one ?from_col ?to_col) ; col +/- 1
                           (not (visited ?to_col ?to_row))
                           (not (at ?knight2 ?to_col ?to_row))
                           (not (at ?knight1 ?to_col ?to_row))
                           (not (= ?knight1 ?knight2))
                           )
        :effect (and (not (at ?knight1 ?from_col ?from_row))
                     (at ?knight1 ?to_col ?to_row)
                     (visited ?to_col ?to_row))
    )
)