;credit: As defined in the book 'An Introduction to the Planning Domain Deﬁnition Language' (Patrik Haslum)
(define (domain knights-tour)
    (:requirements :negative-preconditions :typing) 
    (:types
        location figure - object
    )   
    (:predicates
        (at ?knight - figure ?col ?row - location)
        (visited ?knight - figure ?col ?row - location)
        (diff_by_one ?col ?row)
        (diff_by_two ?col ?row)
    )

    (:action move_2col_1row
        :parameters (?knight - figure ?from_col ?from_row ?to_col ?to_row - location)
        :precondition (and (at ?knight ?from_col ?from_row)
                           (diff_by_two ?from_col ?to_col) ; col +/- 2
                           (diff_by_one ?from_row ?to_row) ; row +/- 1
                           (not (visited ?knight ?to_col ?to_row))
                           (not (at ?knight ?to_col ?to_row)) ;<<<<<<<<<<<<<< COMMENT OUT THIS LINE...
                      )
        :effect (and (not (at ?knight ?from_col ?from_row))
                     (at ?knight ?to_col ?to_row)
                     (visited ?knight ?to_col ?to_row))
    )
    (:action move_2row_1col
        :parameters (?knight - figure ?from_col ?from_row ?to_col ?to_row - location)
        :precondition (and (at ?knight ?from_col ?from_row)
                           (diff_by_two ?from_row ?to_row) ; row +/- 2
                           (diff_by_one ?from_col ?to_col) ; col +/- 1
                           (not (visited  ?knight ?to_col ?to_row))
                           (not (at ?knight ?to_col ?to_row))) ;<<<<<<<<<<<<<< ...AND THIS LINE
        :effect (and (not (at ?knight ?from_col ?from_row))
                     (at ?knight ?to_col ?to_row)
                     (visited  ?knight ?to_col ?to_row))
    )
)
