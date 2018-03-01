(ns certainty-derivative.generator
  (:require [clojure.java.io :as io]
            [clojure.data.json :as json]
            [clojure.string :as string]))

;; Names
;; ===============
(def first-name-choices
  "First names of men and women, pulled from the US 2000 census. https://github.com/dariusk/corpora"
  (get (json/read-str
        (slurp
         (io/as-file
          (io/resource "firstNames.json"))))
       "firstNames"))

(defn first-name []
  (rand-nth first-name-choices))

(def last-name-choices
  "Last names of people, pulled from the US 2000 census. https://github.com/dariusk/corpora"
  (get (json/read-str
        (slurp
         (io/as-file
          (io/resource "lastNames.json"))))
       "lastNames"))

(defn last-name []
  (rand-nth last-name-choices))


;; Genders
;; ===============
;; My solution to the complexities of non-binary gender is informed by these resources.
;;
;; https://civic.mit.edu/blog/kanarinka/a-primer-on-non-binary-gender-and-big-data
;; http://www.hrc.org/resources/collecting-transgender-inclusive-gender-data-in-workplace-and-other-surveys
;;
;; There is a case to be made for not collecting this information, or even sticking with a gender binary question (on the theory that there are few enough gender-nonconforming individuals that to identify as such comes with risks). The assumptions I settled on are:
;;
;; - assume that the data comes from a pulldown menu with some binary and non-binary options, and
;; - assume most people choose from the binary options, and
;; - assume there is no reason to to record transgender status

(def non-binary-gender-options ["Prefer not to say"
                                "Non-binary / third gender"])

(defn gender []
  (let [choice (rand-int 1000)]
    (cond (< choice 5) (rand-nth non-binary-gender-options)
          (even? choice) "f"
          :else "m")))


;; Color
;; ================
(def color-choices
  "For simplicity, just primary and secondary hues."
  ["red" "blue" "purple" "yellow" "orange" "green"])

(defn favorite-color []
  (rand-nth color-choices))


;; Date of birth
;; ===================
(defn year []
  "Assume no one is older than 120."
  (- 2018 (rand-int 120)))

(defn month []
  (- 12 (rand-int 12)))

(defn day []
  "A proper day-of-month generator seems excessive for this use case."
  (- 28 (rand-int 28)))

(defn date-of-birth []
  (str (year) "-" (month) "-" (day)))


;; Generators
;; ==============
(defn sample-data []
  [(last-name)
   (first-name)
   (gender)
   (favorite-color)
   (date-of-birth)])

(defn sample-comma-delinated-row []
  (string/join ", " (sample-data)))

(defn sample-space-delinated-row []
  (string/join " " (sample-data)))

(defn sample-pipe-delinated-row []
  (string/join " | " (sample-data)))
