(ns certainty-derivative.viewer-test
  (:require [certainty-derivative.viewer :refer :all]
            [certainty-derivative.transformer :refer [string->date parse-row]]
            [clojure.test :refer [deftest testing is]]
            [clojure.string :as string]))

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

(deftest test-last-name-sort
  (let [sorted (sort-by-last-name-descending sample)]
    (is (= "Wright" ((first sorted) :certainty-derivative.record/last-name)))
    (is (= "Alberts" ((last sorted) :certainty-derivative.record/last-name)))
    ))

(defn is-female? [record]
  (= "f" (record :certainty-derivative.record/gender)))

(deftest test-gender-sort
  (let [expected '("Cunningham" "Wright" "Alberts" "Alexander" "Henry")]
    (is (= expected (->> sample
                         sort-by-gender-and-last-name-ascending
                         (map :certainty-derivative.record/last-name))))))

(deftest test-dob-sort
  (let [expected '("Alberts" "Henry" "Wright" "Alexander" "Cunningham")]
    (is (= expected (->> sample
                         sort-by-date-of-birth-ascending
                         (map :certainty-derivative.record/last-name))))))

(defn includes-each? [s coll]
  (every? #(string/includes? s %) coll))

(deftest test-friendly-records
  (let [messages (map friendly-format sample)]
    (is (includes-each? (nth messages 0)
                        ["Mya" "Alexander" "Their" "green" "03/23/1979"]))
    (is (includes-each? (nth messages 1)
                        ["Giovanni" "Henry" "His" "orange" "07/16/1941"]))
    (is (includes-each? (nth messages 2)
                        ["Camila" "Wright" "Her" "orange" "03/28/1970"]))
    (is (includes-each? (nth messages 3)
                        ["Paul" "Cunningham" "Her" "green" "07/19/1995"]))
    (is (includes-each? (nth messages 4)
                        ["Margaret" "Alberts" "His" "purple" "09/02/1902"]))))
