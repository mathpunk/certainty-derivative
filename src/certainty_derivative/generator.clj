(ns certainty-derivative.generator
  (:require [clojure.data.json :as json]
            [clojure.java.io :as io]
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
;; - the data comes from a pulldown menu with some binary and non-binary options, and
;; - most people choose from the binary options, and
;; - some people might decline to adjust the menu, and
;; - there is no reason to to record transgender status

(def non-binary-gender-options ["x"       ;; "Prefer not to say"
                                "nb/3rd"  ;; "Non-binary / 3rd gender"
                                ])

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

(defn generate-sample-data [n]
  (repeatedly n sample-data))

(defn generate-sample-rows [n option]
  (let [delineator (cond (= option :comma) ", "
                         (= option :space) " "
                         (= option :pipe) " | ")]
    (map #(string/join delineator %) (generate-sample-data n))))

(defn clean-test-data [filename]
  (let [file (-> filename
                 (io/resource)
                 (io/as-file))]
    (if (.exists file)
      (io/delete-file file)
      nil)))

(defn generate-test-data [n]
  (doseq [file ["001.txt" "002.txt" "003.txt"]]
    (clean-test-data file)) ;; TODO: Investigate fixtures
  (doseq [row (generate-sample-rows n :comma)]
    (spit "./resources/001.txt" (str row "\n") :append true))
  (doseq [row (generate-sample-rows n :space)]
    (spit "./resources/002.txt" (str row "\n") :append true))
  (doseq [row (generate-sample-rows n :pipe)]
    (spit "./resources/003.txt" (str row "\n") :append true)))
