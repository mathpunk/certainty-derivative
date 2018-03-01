(ns certainty-derivative.view-test
  (:require [certainty-derivative.view :refer :all]
            [clojure.test :refer [deftest testing is]]))

(def sample [{:last-name "Alexander", :first-name "Mya", :gender "f",
              :favorite-color "green", :date-of-birth "1979-3-23"}
             {:last-name "Henry", :first-name "Giovanni", :gender "m",
              :favorite-color "orange", :date-of-birth "1941-7-16"}
             {:last-name "Wright", :first-name "Camila", :gender "f",
              :favorite-color "orange", :date-of-birth "1970-3-28"}
             {:last-name "Cunningham", :first-name "Paul", :gender "f",
              :favorite-color "green", :date-of-birth "1995-7-19"}
             {:last-name "Alberts", :first-name "Margaret", :gender "m",
              :favorite-color "purple", :date-of-birth "1902-9-2"}])

(deftest last-name-sort
  (let [sorted (sort-by-last-name-descending sample)]
    (is (= "Alberts" ((last sorted) :last-name)))
    (is (= "Wright" ((first sorted) :last-name)))))
