(ns certainty-derivative.viewer.render-test
  (:require [certainty-derivative.viewer.render :refer :all]
            [certainty-derivative.record.example :as example]
            [certainty-derivative.loader.transform :refer [parse-row]]
            [clojure.test :refer [deftest testing is]]
            [certainty-derivative.viewer.format :as format]
            [clojure.string :as string]))


(deftest test-rendering
  (let [records [(parse-row example/example-comma-row)]
        output (atom [])]
    (with-redefs [out (fn [s] (swap! output conj s))] ;; Mock io of render
      (render records)
      (let [printed (first @output)]
        (is (string? printed))
        (string/includes? printed example/example-first-name)
        (string/includes? printed example/example-last-name)
        (string/includes? printed example/example-favorite-color)
        (string/includes? printed "His")
        (string/includes? printed "(M)")
        (string/includes? printed "03/23/1976")))))
