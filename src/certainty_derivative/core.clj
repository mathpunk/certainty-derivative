(ns certainty-derivative.core
  (:require [certainty-derivative.loader.read :refer [read-files]]
            [certainty-derivative.options :as options]
            [certainty-derivative.viewer.render :refer [render]]))

(defn find-records [args]
  (let [filenames (get (options/parse args) :arguments)]
    (apply read-files filenames)))

(defn view [& args]
  (let [records (find-records args)
        sorted (options/dispatch-sort (options/parse args) records)]
    (render sorted)))


