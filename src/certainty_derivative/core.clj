(ns certainty-derivative.core
  (:require [certainty-derivative.loader.read :refer [read-files]]
            [certainty-derivative.options :as options]
            [certainty-derivative.viewer.render :refer [render]]
            [clojure.string :as string]))


(defn find-records [args]
  (let [filenames (get (options/parse args) :arguments)]
    (apply read-files filenames)))

(defn view [& args]
  (if-let [errors (get (options/parse args) :errors)]
    (println (string/join "\n" errors))
    (let [records (find-records args)
          sorted (options/dispatch-sort (options/parse args) records)]
      (render sorted))))
