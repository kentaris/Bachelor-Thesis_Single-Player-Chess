
;A problem definition - for the switch example: P5
(define (problem turn_it_off)
    (:domain switch) ;domain hat it is associated with
    
    (:init ;defines the initial state of the problem instance (by listing all facts that are true in that state): every fact not listed here is assumed to be false
        (switch_is_on)
    )
    
    (:goal (switch_is_off)) ;condition that must be satisfied at the end of a valid plan. the goal section has the ame form as an action precondition
)

;
