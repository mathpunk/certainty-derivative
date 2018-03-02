(ns certainty-derivative.record
  (:require [clojure.spec.alpha :as s]
            [java-time :as time]))

(s/def ::last-name string?)
(s/def ::first-name string?)
(s/def ::gender string?)
(s/def ::favorite-color string?)
(s/def ::date-of-birth time/local-date?)

(s/def ::row
  (s/keys :req [::last-name ::first-name ::gender
                ::favorite-color ::date-of-birth]))
