(ns certainty-derivative.viewer.render-test
  (:require [certainty-derivative.loader.transform :refer [parse-row]]
            [certainty-derivative.record.example :as example]
            [certainty-derivative.viewer.render :refer :all]
            [clojure.string :as string]
            [clojure.test :refer [deftest is]]))

(deftest test-rendering
  (let [records [(parse-row example/example-comma-row)]
        output (atom [])]
    (with-redefs [out (fn [s] (swap! output conj s))] ;; Mock the io of render
      (render records)
      (let [printed (first @output)]
        (is (string? printed))
        (is (string/includes? printed example/example-first-name))
        (is (string/includes? printed example/example-last-name))
        (is (string/includes? printed example/example-favorite-color))
        (is (string/includes? printed "His"))
        (is (string/includes? printed "(male)"))
        (is (string/includes? printed "03/23/1976"))))))
