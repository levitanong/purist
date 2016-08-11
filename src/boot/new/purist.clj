(ns boot.new.purist
  (:require [boot.new.templates :refer [renderer name-to-path ->files]]))

(def render (renderer "purist"))

(defn purist
  "FIXME: write documentation"
  [name]
  (let [data {:name name
              :sanitized (name-to-path name)}]
    (println "Generating fresh 'boot new' purist project.")
    (->files data
             ["src/{{sanitized}}/build.boot" (render "build.boot" data)]
             ["src/{{sanitized}}/foo.clj" (render "foo.clj" data)])))
