(ns assignment3-comp348.db
  (:require [clojure.string :as str]))

;; Define the database schema
(def students-db (atom []))
(def courses-db (atom []))
(def grades-db (atom []))

;; Load data from file into a data structure
(defn load-data [filename]
  (let [content (slurp filename)
        lines (str/split-lines content)]
    (map #(str/split % #"\|") lines)))

;; Load data into memory
(reset! students-db (load-data "src/assignment3_comp348/studs.txt"))
(reset! courses-db (load-data "src/assignment3_comp348/courses.txt"))
(reset! grades-db (load-data "src/assignment3_comp348/grades.txt"))

;; Helper function to convert letter grades to numerical values
(def grade-map
  {"A+" 4.3 "A" 4 "A-" 3.7
   "B+" 3.3 "B" 3 "B-" 2.7
   "C+" 2.3 "C" 2 "C-" 1.7
   "D+" 1.3 "D" 1 "D-" 0.7
   "F" 0})

;; Display Courses
(defn display-courses []
  (doseq [course @courses-db]
    (println course)))

;; Display Students
(defn display-students []
  (doseq [student @students-db]
    (println student)))

;; Display Grades
(defn display-grades []
  (doseq [grade @grades-db]
    (println grade)))

;; Display Student Record
(defn display-student-record []
  (print "Enter student ID: ")
  (flush)
  (let [student-id (read-line)
        student (first (filter #(= (first %) student-id) @students-db))]
    (when student
      (println student)
      (doseq [grade (filter #(= (first %) student-id) @grades-db)]
        (let [[_ course-id _ grade-letter] grade
              [course-name _ _ _ description] (first (filter #(= (first %) course-id) @courses-db))]
          (println [course-name description (last grade)]))))))

;; Calculate GPA
(defn calculate-gpa []
  (print "Enter student ID: ")
  (flush)
  (let [student-id (read-line)
        student-grades (filter #(= (first %) student-id) @grades-db)
        courses (map second student-grades)
        credits (reduce + (map #(get-in (get @courses-db %) [3]) courses))
        weighted-grades (doall (map (fn [grade]
                                      (let [[_ _ _ grade-letter] grade]
                                        (* (grade-map grade-letter) (get-in (get @courses-db (second grade)) [3]))))
                                    student-grades))
        gpa (if (pos? credits)
              (/ (reduce + weighted-grades) credits)
              0.0)]
    (println "GPA:" gpa)))

;; Calculate Course Average
(defn calculate-course-average []
  (let [course-grades (group-by #(nth % 1) @grades-db)]
    (doseq [[course-id grades] course-grades]
      (let [course (first (filter #(= (first %) course-id) @courses-db))
            numerical-grades (map #(grade-map (nth % 3)) grades)
            average (if (seq numerical-grades)
                      (/ (reduce + numerical-grades) (count numerical-grades))
                      0)]
        (println [(get-in course [1 2]) (last course) average])))))

;; Export the necessary functions for use in menu.clj
(defn initialize-db []
  (reset! students-db (load-data "src/assignment3_comp348/studs.txt"))
  (reset! courses-db (load-data "src/assignment3_comp348/courses.txt"))
  (reset! grades-db (load-data "src/assignment3_comp348/grades.txt")))

(defn initialize []
  (initialize-db))
