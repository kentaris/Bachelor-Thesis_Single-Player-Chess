;problem definition and domain definiion are 2 separate files that are given as input
;A domain definition - kind of planning Hello World: P2ff
(define (domain switch)
    ;:requirements indicate which features of PDDL the domain uses (what kind of planning problem it is)
    (:requirements :strips) ;domain is of the simplest form/ for strips the :goal is either a singe fact or a conjunction of facts
    (:predicates (switch_is_on)(switch_is_off)) ;contains the list of the ,odel's state variables (they are binary): 2 variables = 4 possile states
    
    (:action switch_on
    :precondition (switch_is_off)
    :effect (and(switch_is_on)(not (switch_is_off)))
    )
    
    (:action switch_off
    :precondition (switch_is_on)
    :effect (and(switch_is_off)(not (switch_is_on)))
    )
)
