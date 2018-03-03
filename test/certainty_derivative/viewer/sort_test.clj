(ns certainty-derivative.viewer.sort-test
  (:require [certainty-derivative.loader.transform :refer [string->date]]
            [certainty-derivative.viewer.sort :refer :all]
            [clojure.test :refer [deftest is testing]]))

(def sample-input [{:certainty-derivative.record/last-name "Alexander"
                    :certainty-derivative.record/first-name "Mya"
                    :certainty-derivative.record/gender "Decline to state"
                    :certainty-derivative.record/favorite-color "green"
                    :certainty-derivative.record/date-of-birth "1979-3-23"},
                   {:certainty-derivative.record/last-name "Henry",
                    :certainty-derivative.record/first-name "Giovanni",
                    :certainty-derivative.record/gender "m",
                    :certainty-derivative.record/favorite-color "orange",
                    :certainty-derivative.record/date-of-birth "1941-7-16"},
                   {:certainty-derivative.record/last-name "Wright",
                    :certainty-derivative.record/first-name "Camila",
                    :certainty-derivative.record/gender "f",
                    :certainty-derivative.record/favorite-color "orange",
                    :certainty-derivative.record/date-of-birth "1970-3-28"},
                   {:certainty-derivative.record/last-name "Cunningham",
                    :certainty-derivative.record/first-name "Paul",
                    :certainty-derivative.record/gender "f",
                    :certainty-derivative.record/favorite-color "green",
                    :certainty-derivative.record/date-of-birth "1995-7-19"},
                   {:certainty-derivative.record/last-name "Alberts",
                    :certainty-derivative.record/first-name "Margaret",
                    :certainty-derivative.record/gender "m",
                    :certainty-derivative.record/favorite-color "purple",
                    :certainty-derivative.record/date-of-birth "1902-9-2"}])

(def sample (map (fn [record]
                   (update-in record
                              [:certainty-derivative.record/date-of-birth]
                              string->date)) sample-input))

(deftest test-last-name-asscending-sort
  (let [expected '("Alberts" "Alexander" "Cunningham" "Henry" "Wright")
        sorted (by-last-name sample)]
    (is (= expected (map :certainty-derivative.record/last-name sorted)))))

(defn is-female? [record]
  (= "f" (record :certainty-derivative.record/gender)))

(deftest test-gender-sort
  (let [expected '("Cunningham" "Wright" "Alberts" "Alexander" "Henry")
        sorted (by-gender-and-last-name sample)]
    (is (= expected (map :certainty-derivative.record/last-name sorted)))))

(deftest test-dob-sort
  (let [expected '("Alberts" "Henry" "Wright" "Alexander" "Cunningham")
        sorted (by-date-of-birth sample)]
    (is (= expected (map :certainty-derivative.record/last-name sorted)))))

(deftest test-sort-dispatch
  (testing "last name ascending"
    (let [expected '("Alberts" "Alexander" "Cunningham" "Henry" "Wright")
          sorted (dispatch-sort {:options {:sort "lname" :reverse "false"}} sample)]
      (is (= expected (map :certainty-derivative.record/last-name sorted)))))
  (testing "last name descending"
    (let [expected (reverse '("Alberts" "Alexander" "Cunningham" "Henry" "Wright"))
          sorted (dispatch-sort {:options {:sort "lname" :reverse "true"}} sample)]
      (is (= expected (map :certainty-derivative.record/last-name sorted)))))
  (testing "women first, otherwise by last name"
    (let [expected '("Cunningham" "Wright" "Alberts" "Alexander" "Henry")
          sorted (dispatch-sort {:options {:sort "women" :reverse "false"}} sample)]
      (is (= expected (map :certainty-derivative.record/last-name sorted)))))
  (testing "date of birth ascending"
    (let [expected '("Alberts" "Henry" "Wright" "Alexander" "Cunningham")
          sorted (dispatch-sort {:options {:sort "dob" :reverse "false"}} sample)]
      (is (= expected (map :certainty-derivative.record/last-name sorted))))))




