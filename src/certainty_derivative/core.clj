(ns certainty-derivative.core
  (:require [certainty-derivative.loader.read :refer [read-files]]
            [certainty-derivative.options :as options]
            [certainty-derivative.viewer.render :refer [render]]
            [certainty-derivative.viewer.format :as format]
            [certainty-derivative.viewer.sort :as sort]))

(defn find-records [args]
  (let [filenames (get (options/parse args) :arguments)]
    (apply read-files filenames)))

(defn view [& args]
  (let [records (find-records args)
        sorted (sort/dispatch-sort (options/parse args) records)]
    (render sorted)))


