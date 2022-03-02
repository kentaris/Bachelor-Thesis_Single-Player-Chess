;problem definition and domain definiion are 2 separate files that are given as input
;A domain definition - kind of planning Hello World: P2ff
(define (domain switch)
    ;:requirements indicate which features of PDDL the domain uses (what kind of planning problem it is)
    (:requirements :strips) ;domain is of the simplest form/ for strips the :goal is either a singe fact or a conjunction of facts
    (:predicates (switch_is_on)(switch_is_off)) ;contains the list of the ,odel's state variables (they are binary): 2 variables = 4 possile states
    
    (:action switch_on
    :precondition (switch_is_off)
    :effect (and(switch_is_on)
                (not (switch_is_off)))
    )
    
    (:action switch_off
    :precondition (switch_is_on)
    :effect (and(switch_is_off)
                (not (switch_is_on)))
    )
)


;A problem definition - for the switch example: P5
(define (problem turn_it_off)
    (:domain switch) ;domain hat it is associated with
    
    (:init ;defines the initial state of the problem instance (by listing all facts that are true in that state): every fact not listed here is assumed to be false
        (switch_is_on)
    )
    
    (:goal (switch_is_off)) ;condition that must be satisfied at the end of a valid plan. the goal section has the ame form as an action precondition
)

;Knights move problem formulation: move knight from A8 to B6 (wich is a valid knights move)
(:action move_A8_to_B6
    :precondition (and (at_A8)
                       (not (visited_B6))) ;negation of the predicate is allowed if if the keyword :negative-preconditions is added to the :requirements section
    :effect (and (not (at_A8))
                 (at_B6)
                 (visited_B6))
)

;there are 336 valid knights moves on the chess board thus we could model all of them like this using 64 predicates for the possible current locations of the knight and 64 to keep track of the squares visited
;However, these actions are all very similar: they diﬀer only by which two squares the Knight moves between.
;Parameterising predicates and actions allows us to write a smaller number of predicates and actions—in the case of the Knight’s tour, only one action—that are then instantiated with collection of possible objects.

;The following is a parameterised version of the Knight’s Tour:
(define (domain knights-tour)
    (:requirements :negative-preconditions)
    (:predicates
        (at ?square)
        (visited ?square)
        (valid_move ?square_from ?square_to)
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

;The format of a (simple) domain definition is:
(define (domain DOMAIN_NAME)
  (:requirements [:strips] [:equality] [:typing] [:adl])
  (:predicates (PREDICATE_1_NAME ?A1 ?A2 ... ?AN)
               (PREDICATE_2_NAME ?A1 ?A2 ... ?AN)
	       ...)

  (:action ACTION_1_NAME
    [:parameters (?P1 ?P2 ... ?PN)]
    [:precondition PRECOND_FORMULA]
    [:effect EFFECT_FORMULA]
   )

  (:action ACTION_2_NAME
    ...)

  ...)


  