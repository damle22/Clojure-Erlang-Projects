(ns assignment3-comp348.menu
  (:require [assignment3-comp348.db :refer [display-courses
                                            display-students
                                            display-grades
                                            display-student-record
                                            calculate-gpa
                                            calculate-course-average]]))

;; Main menu loop
(defn main-loop []
  (loop []
    (println "*** SIS Menu ***")
    (println "------------------")
    (println "1. Display Courses")
    (println "2. Display Students")
    (println "3. Display Grades")
    (println "4. Display Student Record")
    (println "5. Calculate GPA")
    (println "6. Course Average")
    (println "7. Exit")
    (print "Enter an option? ")
    (flush)
    (let [option (read-line)]
      (case option
        "1" (do (display-courses) (recur))
        "2" (do (display-students) (recur))
        "3" (do (display-grades) (recur))
        "4" (do (display-student-record) (recur))
        "5" (do (calculate-gpa) (recur))
        "6" (do (calculate-course-average) (recur))
        "7" (println "Exiting...")
        (do (println "Invalid option. Please try again.")
            (recur))))))

;; Export the main-loop function for use in app.clj
(defn main []
  (main-loop))
